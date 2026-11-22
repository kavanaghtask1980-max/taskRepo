package tests.smoke;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import api.JenkinsApiClient;
import base.BaseTest;

public class JenkinsSmokeTests extends BaseTest {

    @Test(groups = {"smoke"}, description = "Verify Jenkins REST API health and baseline seeded state")
    public void verifyApiReadinessAndSeededJobs(ITestContext context) {
        JenkinsApiClient api = new JenkinsApiClient();
        Assert.assertTrue(
            api.getAllJobNames().contains("seed-smoke-freestyle"),
            "Expected baseline seeded job 'seed-smoke-freestyle' to exist via REST API"
        );
    }

    @Test(groups = {"smoke"}, description = "Verify authenticated Jenkins dashboard loads with core navigation")
    public void verifyDashboardAndPrimaryNavigation(ITestContext context) {
        Assert.assertTrue(
            app.getPageTitle().contains("Dashboard"),
            "Expected authenticated page title to contain 'Dashboard', got: " + app.getPageTitle()
        );
        Assert.assertTrue(
            app.isMenuLinkDisplayed("dashboard_newItem_css"),
            "Expected 'New Item' navigation link to be visible on dashboard"
        );
    }
}
