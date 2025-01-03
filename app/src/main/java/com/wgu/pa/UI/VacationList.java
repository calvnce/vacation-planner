package com.wgu.pa.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgu.pa.R;
import com.wgu.pa.database.Repository;
import com.wgu.pa.entities.Excursion;
import com.wgu.pa.entities.Vacation;
import com.wgu.pa.service.ReportGenerator;

import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        //make the Add Vacations button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        //creates a RecyclerView list and populates it with all vacations in the db
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    //make menu and populate it with menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);

        // Perform search
        search(menu);

        return true;
    }

    //needs to be duplicated here because it is outside of onCreate()
    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getmAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    //define what will happen when a menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Takes user back to home
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        // Navigate to Reports page
        if (item.getItemId() == R.id.myreport) {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
            return true;
        }

        //manually adds sample vacations and excursions to db when user clicks My Sample
        if (item.getItemId() == R.id.mysample) {
            repository = new Repository(getApplication());

            // Insert vacations
            Vacation vacation = new Vacation(0, "Panama", "Marriott", "12/30/24", "12/31/24");
            repository.insert(vacation);
            vacation = new Vacation(0, "China", "Hilton", "12/30/24", "12/30/24");
            repository.insert(vacation);
            vacation = new Vacation(0, "Japan", "Ritz-Carlton", "01/05/25", "01/10/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "Italy", "Four Seasons", "02/14/25", "02/20/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "Mexico", "Hyatt", "03/01/25", "03/05/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "Australia", "InterContinental", "04/10/25", "04/15/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "Canada", "Fairmont", "05/20/25", "05/25/25");
            repository.insert(vacation);

            // Insert corresponding excursions
            Excursion excursion = new Excursion(0, "Cycling", 1, "12/30/24");
            repository.insert(excursion);
            excursion = new Excursion(0, "Wine Tasting", 2, "12/30/24");
            repository.insert(excursion);
            excursion = new Excursion(0, "Cherry Blossom Viewing", 3, "01/06/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Temple Visit", 3, "01/07/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Gondola Ride", 4, "02/15/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Cooking Class", 4, "02/16/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Tequila Tasting", 5, "03/02/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Beach Volleyball", 5, "03/03/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Great Barrier Reef Diving", 6, "04/11/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Wildlife Safari", 6, "04/12/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Niagara Falls Tour", 7, "05/21/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Museum Tour", 7, "05/22/25");
            repository.insert(excursion);

            // Refresh RecyclerView
            updateVacationListView();
            return true;
        }

        return true;
    }

    private void search(Menu menu) {
        // Setup SearchView
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterVacations(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterVacations(newText);
                return true;
            }
        });
    }

    private void filterVacations(String query) {
        List<Vacation> vacations = repository.getmAllVacations();
        List<Vacation> filteredList = new ArrayList<>();

        // Filter Vacation by matching query with title, hotel, or date
        for (Vacation vacation : vacations) {
            if (vacation.getVacationTitle().toLowerCase().contains(query.toLowerCase()) ||
                    vacation.getVacationHotel().toLowerCase().contains(query.toLowerCase()) ||
                    vacation.getStartDate().toLowerCase().contains(query.toLowerCase()) ||
                    vacation.getEndDate().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(vacation);
            }
        }

        // Update the RecyclerView with filtered results
        vacationAdapter.setVacations(filteredList);
    }

    private void updateVacationListView() {
        List<Vacation> allVacations = repository.getmAllVacations();
        // Update the RecyclerView with loaded results
        vacationAdapter.setVacations(allVacations);
    }
}