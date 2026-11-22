package tests.regression;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;
import data.JsonDataProvider;

public class JobLifecycleTests extends BaseTest {

    @Test(
        groups = {"regression"},
        dataProvider = "jenkinsJobData",
        dataProviderClass = JsonDataProvider.class,
        description = "Data-driven E2E Freestyle job creation, verification, and deletion"
    )
    public void verifyFreestyleJobLifecycle(JSONObject data, ITestContext context) {
        String testName = (String) data.get("testName");
        String jobPrefix = (String) data.get("jobPrefix");
        String description = (String) data.get("description");
        String dynamicJobName = jobPrefix + "-" + System.currentTimeMillis();

        app.log("Executing dataset [" + testName + "] -> Job: " + dynamicJobName);

        app.createFreestyleJob(dynamicJobName, description);
        Assert.assertTrue(
            app.getPageTitle().contains(dynamicJobName),
            "Expected job page title to contain: " + dynamicJobName
        );

        app.deleteCurrentJob();
        Assert.assertFalse(
            app.isJobListedOnDashboard(dynamicJobName),
            "Expected deleted job '" + dynamicJobName + "' to be removed from dashboard"
        );
    }
}
