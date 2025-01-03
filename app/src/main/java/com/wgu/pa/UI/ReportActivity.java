package com.wgu.pa.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgu.pa.database.Repository;
import com.wgu.pa.R;
import com.wgu.pa.entities.Excursion;
import com.wgu.pa.entities.Report;
import com.wgu.pa.entities.Vacation;
import com.wgu.pa.service.ReportGenerator;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        repository = new Repository(getApplication());

        RecyclerView recyclerView = findViewById(R.id.reportRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Vacation> vacations = repository.getmAllVacations();
        List<Excursion> excursions = repository.getmAllExcursions();

        // Prepare VacationReport data
        List<Report> vacationReports = new ArrayList<>();
        for (Vacation vacation : vacations) {
            List<Excursion> associatedExcursions = new ArrayList<>();
            for (Excursion excursion : excursions) {
                if (excursion.getVacationID() == vacation.getVacationId()) {
                    associatedExcursions.add(excursion);
                }
            }
            vacationReports.add(new Report(vacation, associatedExcursions));
        }

        ReportAdapter adapter = new ReportAdapter(this, vacationReports);
        recyclerView.setAdapter(adapter);

        FloatingActionButton downloadButton = findViewById(R.id.floatingActionButtonReportDownload);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "Vacation and Excursions Report";
                String path = ReportGenerator.generateReport(
                        ReportActivity.this,
                        title,
                        vacations,
                        excursions);

                Toast.makeText(ReportActivity.this, "Report saved to: " + path, Toast.LENGTH_LONG).show();
            }
        });
    }
}