package testcases;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import base.BaseTest;

public class JenkinsSmokeTests extends BaseTest {

    @Test(priority = 1, description = "Verify authenticated user lands on Jenkins Dashboard with core navigation")
    public void verifyDashboardAndAuth(ITestContext context) {
        app.log("Running Smoke Test 1: verifyDashboardAndAuth");

        Assert.assertTrue(
            app.getPageTitle().contains("Dashboard"),
            "Expected Dashboard title after login, but found: " + app.getPageTitle()
        );
        Assert.assertTrue(
            app.isMenuLinkDisplayed("dashboard_newItem_css"),
            "Expected 'New Item' link to be visible on Dashboard"
        );
        Assert.assertTrue(
            app.isMenuLinkDisplayed("dashboard_manageJenkins_css"),
            "Expected 'Manage Jenkins' link to be visible for authenticated admin"
        );
    }

    @Test(priority = 2, description = "Verify clicking New Item opens the Create Item page")
    public void verifyNewItemPageNavigation(ITestContext context) {
        app.log("Running Smoke Test 2: verifyNewItemPageNavigation");

        app.getElement("dashboard_newItem_css").click();
        app.waitForPageToLoad();

        Assert.assertTrue(
            app.isMenuLinkDisplayed("newItem_nameInput_css"),
            "Expected item name input field on New Item page"
        );
    }

    @Test(priority = 3, description = "Verify global search bar navigates directly to target job")
    public void verifyGlobalSearch(ITestContext context) {
        app.log("Running Smoke Test 3: verifyGlobalSearch");

        app.searchJenkins("test001");

        Assert.assertTrue(
            app.getPageTitle().contains("test001"),
            "Expected search to navigate to job 'test001', but title was: " + app.getPageTitle()
        );
    }
}
