package testcases;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class JenkinsResetTests extends BaseTest {

    @Test(description = "Wipe all non-CI jobs to restore Jenkins to a clean vanilla state")
    public void resetEnvironmentToVanillaState(ITestContext context) {
        String protectedCiJob = "test001";

        app.resetJenkinsToVanillaState(protectedCiJob);

        Assert.assertTrue(
            app.isJobListedOnDashboard(protectedCiJob),
            "Expected protected CI runner job '" + protectedCiJob + "' to remain intact"
        );
    }
}
