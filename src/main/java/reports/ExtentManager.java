package reports;

import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    static ExtentReports reports;
    public static String screenshotFolderPath;

    public static ExtentReports getReports() {
        if (reports == null) {
            reports = new ExtentReports();

            // 1. Timestamped archive folder (keeps full run history)
            Date d = new Date();
            String timestamp = d.toString().replaceAll(":", "-");
            String reportFolderPath = System.getProperty("user.dir") + "/reports/" + timestamp;
            screenshotFolderPath = reportFolderPath + "/screenshots";

            new File(screenshotFolderPath).mkdirs();

            ExtentSparkReporter archiveReporter = new ExtentSparkReporter(reportFolderPath + "/Index.html");
            configureReporter(archiveReporter);

            // 2. Predictable "latest" folder (for Jenkins HTML Publisher plugin)
            String latestFolderPath = System.getProperty("user.dir") + "/reports/latest";
            new File(latestFolderPath).mkdirs();

            ExtentSparkReporter latestReporter = new ExtentSparkReporter(latestFolderPath + "/Index.html");
            configureReporter(latestReporter);

            reports.attachReporter(archiveReporter, latestReporter);
        }

        return reports;
    }

    private static void configureReporter(ExtentSparkReporter reporter) {
        reporter.config().setReportName("Jenkins Automation Suite");
        reporter.config().setDocumentTitle("Jenkins E2E Automation Report");
        reporter.config().setTheme(Theme.STANDARD);
        reporter.config().setEncoding("utf-8");
    }
}