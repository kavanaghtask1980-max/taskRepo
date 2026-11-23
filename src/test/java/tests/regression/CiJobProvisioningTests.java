package tests.regression;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import api.JenkinsApiClient;
import base.BaseTest;
import config.ConfigManager;
import data.JsonDataProvider;

public class CiJobProvisioningTests extends BaseTest {

    @Test(
        groups = {"regression"},
        dataProvider = "ciJobData",
        dataProviderClass = JsonDataProvider.class,
        description = "Wipe Jenkins to 100% vanilla state and provision full CI Freestyle build job via UI"
    )
    public void verifyFullCiBuildJobProvisioning(JSONObject data, ITestContext context) {
        String testName = (String) data.get("testName");
        String jobName = (String) data.get("jobName");
        String description = (String) data.get("description");
        String gitUrl = (String) data.get("gitUrl");
        String branch = (String) data.get("branch");
        String pollSchedule = (String) data.get("pollSchedule");
        String pomPath = (String) data.get("pomPath");
        String mavenGoals = (String) data.get("mavenGoals");
        String artifactPattern = (String) data.get("artifactPattern");

        app.log("Executing [" + testName + "] -> Wiping Jenkins to 100% vanilla state via REST API");
        JenkinsApiClient api = new JenkinsApiClient();
        int wiped = api.wipeAllJobs();
        app.log("Vanilla wipe complete. Removed " + wiped + " existing jobs.");

        app.driver.get(ConfigManager.getEnv("url"));
        app.waitForPageToLoad();

        // Provision full CI job through UI
        app.createConfiguredCiJob(
            jobName, description, gitUrl, branch, pollSchedule, pomPath, mavenGoals, artifactPattern
        );

        Assert.assertTrue(
            app.getPageTitle().contains(jobName),
            "Expected job page title to contain '" + jobName + "', got: " + app.getPageTitle()
        );
        Assert.assertTrue(
            api.getAllJobNames().contains(jobName),
            "Expected provisioned CI job '" + jobName + "' to be verified via REST API"
        );
    }
}
