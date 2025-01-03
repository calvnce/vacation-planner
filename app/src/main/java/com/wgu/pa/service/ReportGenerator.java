package com.wgu.pa.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.wgu.pa.entities.Excursion;
import com.wgu.pa.entities.Vacation;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportGenerator {

    public static String generateReport(Context context, String title, List<Vacation> vacations, List<Excursion> excursions) {
        if (vacations.isEmpty()) return null;
        try {
            // Create a directory in external storage
            // Create a directory in the Downloads folder
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File reportsDir = new File(downloadsDir, "Reports");

            // Create the directory if it doesn't exist
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            // Create a file for the report
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File file = new File(reportsDir, String.format("%s_%s.pdf", title, timeStamp));

            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            Paragraph titleParagraph = new Paragraph(title)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold();
            document.add(titleParagraph);

            // Add date-time stamp
            Paragraph dateParagraph = new Paragraph("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12);
            document.add(dateParagraph);

            // Add a table for data
            float[] columnWidths = {2, 2, 1, 1};
            Table table = new Table(columnWidths);

            // Set table width to fit the page width
            table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            // Add table headers
            table.addHeaderCell("Vacation Title");
            table.addHeaderCell("Hotel");
            table.addHeaderCell("Start Date");
            table.addHeaderCell("End Date");

            // Populate table data
            for (Vacation vacation : vacations) {
                table.addCell(vacation.getVacationTitle());
                table.addCell(vacation.getVacationHotel());
                table.addCell(vacation.getStartDate());
                table.addCell(vacation.getEndDate());

                // Add excursions as sub-rows
                List<Excursion> excursionList = filterExcursionsByVacation(excursions, vacation.getVacationId());
                if (!excursionList.isEmpty()) {
                    for (Excursion excursion : excursionList) {
                        table.addCell(new Cell(1, 4).add(new Paragraph("Excursion: " + excursion.getExcursionTitle())
                                .setItalic()
                                .setFontSize(10))); // Merged across 4 columns
                    }
                }
            }

            // Add the table to the document
            document.add(table);

            // Close document
            document.close();

            Log.d("ReportGenerator", "Report generated: " + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (Exception e) {
            Log.e("ReportGenerator", "Error generating report", e);
        }
        return null;
    }

    private static List<Excursion> filterExcursionsByVacation(List<Excursion> excursions, int vacationId) {
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : excursions) {
            if (excursion.getVacationID() == vacationId) {
                filteredExcursions.add(excursion);
            }
        }
        return filteredExcursions;
    }

}
