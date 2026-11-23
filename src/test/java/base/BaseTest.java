package base;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import api.JenkinsApiClient;
import config.ConfigManager;
import data.JsonDataProvider;
import keywords.ApplicationKeywords;
import listener.MyTestNGListener;
import reports.ExtentManager;

@Listeners(MyTestNGListener.class)
public class BaseTest {

    public ApplicationKeywords app;
    public ExtentReports rep;
    public ExtentTest test;

    @BeforeSuite(alwaysRun = true)
    public void bootstrapEnvironmentViaApi() {
        JenkinsApiClient api = new JenkinsApiClient();
        String protectedJob = ConfigManager.getEnv("protectedCiJob");
        if (protectedJob == null || protectedJob.isBlank()) {
            protectedJob = "test001";
        }

        int removed = api.resetAllJobsExcept(protectedJob);
        System.out.println("[BeforeSuite API] Vanilla reset complete (preserved '" + protectedJob + "'). Removed " + removed + " jobs.");

        Object[][] seedRows = JsonDataProvider.loadJsonArray("seedJobs.json");
        for (Object[] row : seedRows) {
            JSONObject jobData = (JSONObject) row[0];
            String jobName = (String) jobData.get("jobName");
            String desc = (String) jobData.get("description");
            int status = api.createFreestyleJobViaApi(jobName, desc);
            System.out.println("[BeforeSuite API] Seeded '" + jobName + "' -> HTTP " + status);
        }
    }

    @Parameters("browser")
    @BeforeTest(alwaysRun = true)
    public void BeforeTest(@Optional("Chrome") String browser, ITestContext context) {
        app = new ApplicationKeywords();
        context.setAttribute("app", app);

        rep = ExtentManager.getReports();
        test = rep.createTest(context.getCurrentXmlTest().getName());
        app.setReport(test);

        context.setAttribute("report", rep);
        context.setAttribute("test", test);

        app.openBrowser(browser);
        app.navigate("url");
    }

    @BeforeMethod(alwaysRun = true)
    public void BeforeMethod(ITestContext context) {
        test = (ExtentTest) context.getAttribute("test");
        app = (ApplicationKeywords) context.getAttribute("app");
        rep = (ExtentReports) context.getAttribute("report");
        test.log(Status.INFO, "In BeforeMethod");

        String criticalFailure = (String) context.getAttribute("criticalFailure");
        if (criticalFailure != null && criticalFailure.equals("Y")) {
            test.log(Status.SKIP, "Critical Failure In Previous Tests");
            throw new SkipException("Critical Failure In Previous Tests");
        }
    }

    @AfterTest(alwaysRun = true)
    public void quit(ITestContext context) {
        app = (ApplicationKeywords) context.getAttribute("app");
        if (app != null) {
            app.quit();
        }
        if (test != null) {
            test.log(Status.INFO, "Test Finished");
        }
        if (rep != null) {
            rep.flush();
        }
    }
}
