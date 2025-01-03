package com.wgu.pa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.os.Environment;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.wgu.pa.entities.Excursion;
import com.wgu.pa.entities.Vacation;
import com.wgu.pa.service.ReportGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ReportGeneratorTest {
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.wgu.pa", appContext.getPackageName());
    }


    @Test
    public void whenDataIsAvailable_thenReportShouldBeGenerated() {
        // Prepare test data
        List<Vacation> testVacations = new ArrayList<>();
        testVacations.add(new Vacation(1, "Hawaii", "Beach Resort", "2023-12-01", "2023-12-15"));
        testVacations.add(new Vacation(2, "Japan", "Tokyo Inn", "2024-01-05", "2024-01-20"));

        List<Excursion> testExcursions = new ArrayList<>();
        testExcursions.add(new Excursion(1, "Snorkeling", 1, "2023-12-05"));
        testExcursions.add(new Excursion(2, "Mountain Hiking", 2, "2024-01-10"));

        String reportPath = ReportGenerator.generateReport(context, "Test Report", testVacations, testExcursions);

        // Verify the file creation and content
        assertNotNull("Report file path should not be null", reportPath);
        File reportFile = new File(reportPath);

        // Check if the file was created
        assertTrue("Report file should be created", reportFile.exists());
        assertTrue("Report file should not be empty", reportFile.length() > 0);

        // Clean up the generated report
        reportFile.delete();
    }

    @Test
    public void whenNoData_thenNoReportShouldBeGenerated() {
        // Prepare empty lists
        List<Vacation> emptyVacations = new ArrayList<>();
        List<Excursion> emptyExcursions = new ArrayList<>();

        String reportPath = ReportGenerator.generateReport(context, "Empty Report", emptyVacations, emptyExcursions);

        assertNull("Report file path should be null when no data is provided", reportPath);

        // Verify that no file is created
        File reportFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Reports/Empty Report.pdf");
        assertFalse("Report file should not exist when no data is provided", reportFile.exists());
    }
}