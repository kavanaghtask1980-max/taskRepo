package testcases;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;
import testdata.JsonDataProvider;

public class JobSeedTests extends BaseTest {

    @Test(
        dataProvider = "seedJobData",
        dataProviderClass = JsonDataProvider.class,
        description = "Seed baseline Freestyle jobs onto Jenkins dashboard"
    )
    public void seedBaselineJob(JSONObject data, ITestContext context) {
        String jobName = (String) data.get("jobName");
        String description = (String) data.get("description");

        app.seedFreestyleJob(jobName, description);

        Assert.assertTrue(
            app.isJobListedOnDashboard(jobName),
            "Expected seeded job '" + jobName + "' to be saved on Jenkins dashboard"
        );
    }
}
