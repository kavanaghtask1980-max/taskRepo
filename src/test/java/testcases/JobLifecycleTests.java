package testcases;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import base.BaseTest;

public class JobLifecycleTests extends BaseTest {

    private final String dynamicJobName = "auto-freestyle-" + System.currentTimeMillis();

    @Test(priority = 1, description = "Create a new Freestyle project with description")
    public void createFreestyleProject(ITestContext context) {
        app.createFreestyleJob(
            dynamicJobName,
            "Automated E2E test job created via Selenium + TestNG"
        );

        Assert.assertTrue(
            app.getPageTitle().contains(dynamicJobName),
            "Expected job page title to contain: " + dynamicJobName
        );
    }

    @Test(
        priority = 2,
        dependsOnMethods = "createFreestyleProject",
        description = "Delete the created Freestyle project and verify removal from dashboard"
    )
    public void deleteFreestyleProject(ITestContext context) {
        app.deleteCurrentJob();

        Assert.assertFalse(
            app.isJobListedOnDashboard(dynamicJobName),
            "Expected deleted job '" + dynamicJobName + "' to be removed from dashboard"
        );
    }
}