package reports;

import java.io.File;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    static ExtentReports reports;
    public static String screenshotFolderPath;

    public static ExtentReports getReports() {
        if (reports == null) {
            reports = new ExtentReports();

            String latestFolderPath = System.getProperty("user.dir") + "/reports/latest";
            screenshotFolderPath = latestFolderPath + "/screenshots";
            new File(screenshotFolderPath).mkdirs();

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(latestFolderPath + "/Index.html");
            sparkReporter.config().setReportName("Jenkins Automation Suite");
            sparkReporter.config().setDocumentTitle("Jenkins E2E Automation Report");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("utf-8");

            reports.attachReporter(sparkReporter);
        }

        return reports;
    }
}