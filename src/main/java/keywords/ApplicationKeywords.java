package keywords;

import base.BaseEngine;
import pages.DashboardPage;
import pages.JobConfigPage;
import pages.LoginPage;

public class ApplicationKeywords extends BaseEngine {

    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;
    private final JobConfigPage jobConfigPage;

    public ApplicationKeywords() {
        super();
        this.loginPage = new LoginPage(this);
        this.dashboardPage = new DashboardPage(this);
        this.jobConfigPage = new JobConfigPage(this);
    }

    // --- Navigation & Authentication ---

    public void navigate(String urlKey) {
        loginPage.openAndLoginIfNeeded(urlKey);
    }

    // --- Dashboard & Search Actions ---

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isMenuLinkDisplayed(String locatorKey) {
        return dashboardPage.isMenuLinkDisplayed(locatorKey);
    }

    public void searchJenkins(String query) {
        dashboardPage.search(query);
    }

    public boolean isJobListedOnDashboard(String jobName) {
        return dashboardPage.isJobListed(jobName);
    }

    public void openDashboardJobMenu(String jobName) {
        dashboardPage.openJobContextMenu(jobName);
    }

    public void selectContextMenuOption(String menuOption) {
        dashboardPage.selectContextMenuOption(menuOption);
    }

    // --- Job Lifecycle & Configuration Actions ---

    public void createFreestyleJob(String jobName, String description) {
        jobConfigPage.createFreestyleJob(jobName, description);
    }

    public void deleteCurrentJob() {
        jobConfigPage.deleteCurrentJob();
    }

    public void seedFreestyleJob(String jobName, String description) {
        jobConfigPage.seedFreestyleJobIfMissing(jobName, description, dashboardPage);
    }

    public void openBuildHistoryMenu(String buildNumber) {
        jobConfigPage.openBuildHistoryMenu(buildNumber);
    }
}
