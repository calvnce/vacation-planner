package com.wgu.pa.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.pa.R;
import com.wgu.pa.entities.Report;
import com.wgu.pa.entities.Excursion;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private Context context;
    private List<Report> vacationReports;

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView vacationTitle;
        TextView vacationHotel;
        TextView startDate;
        TextView endDate;
        LinearLayout excursionContainer;

        public ReportViewHolder(View itemView) {
            super(itemView);
            vacationTitle = itemView.findViewById(R.id.vacationTitle);
            vacationHotel = itemView.findViewById(R.id.vacationHotel);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            excursionContainer = itemView.findViewById(R.id.excursionContainer);
        }
    }

    public ReportAdapter(Context context, List<Report> vacationReports) {
        this.context = context;
        this.vacationReports = vacationReports;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_report_list, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = vacationReports.get(position);
        holder.vacationTitle.setText(report.getVacation().getVacationTitle());
        holder.vacationHotel.setText(report.getVacation().getVacationHotel());
        holder.startDate.setText(report.getVacation().getStartDate());
        holder.endDate.setText(report.getVacation().getEndDate());

        // Clear previous excursions to prevent duplication
        holder.excursionContainer.removeAllViews();

        for (Excursion excursion : report.getExcursions()) {
            TextView excursionView = new TextView(context);
            String info = String.format(". %-10s on %s", excursion.getExcursionTitle(), excursion.getExcursionDate());
            excursionView.setText(info);
            excursionView.setTextSize(16);
            excursionView.setPadding(8, 4, 8, 4);
            holder.excursionContainer.addView(excursionView);
        }
    }

    @Override
    public int getItemCount() {
        return vacationReports.size();
    }
}
