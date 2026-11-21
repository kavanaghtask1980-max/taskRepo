package base;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import keywords.ApplicationKeywords;
import reports.ExtentManager;

public class BaseTest {

    public ApplicationKeywords app;
    public ExtentReports rep;
    public ExtentTest test;

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
