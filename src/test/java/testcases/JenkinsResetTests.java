package testcases;

import java.util.List;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import api.JenkinsApiClient;
import base.BaseTest;

public class JenkinsResetTests extends BaseTest {

    @Test(description = "Reset Jenkins to vanilla state via REST API (preserving CI runner)")
    public void resetEnvironmentToVanillaState(ITestContext context) {
        String protectedCiJob = "test001";
        JenkinsApiClient api = new JenkinsApiClient();

        app.log("Executing REST API environment reset (preserving: " + protectedCiJob + ")");
        int removedCount = api.resetAllJobsExcept(protectedCiJob);
        app.log("API Reset complete. Removed " + removedCount + " jobs.");

        // Verify via REST API state
        List<String> remainingJobs = api.getAllJobNames();
        Assert.assertEquals(remainingJobs.size(), 1, "Expected only the protected CI job to remain via API");
        Assert.assertTrue(remainingJobs.contains(protectedCiJob), "Protected CI job missing from API response");

        // Verify via UI dashboard refresh
        app.driver.navigate().refresh();
        app.waitForPageToLoad();
        Assert.assertTrue(
            app.isJobListedOnDashboard(protectedCiJob),
            "Expected protected CI job '" + protectedCiJob + "' to remain visible on UI dashboard"
        );
    }
}
