package keywords;

import base.BaseEngine;
import pages.DashboardPage;
import pages.JobConfigPage;
import pages.LoginPage;
import pages.NewItemPage;

public class ApplicationKeywords extends BaseEngine {

    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;
    private final NewItemPage newItemPage;
    private final JobConfigPage jobConfigPage;

    public ApplicationKeywords() {
        this.loginPage = new LoginPage(this);
        this.dashboardPage = new DashboardPage(this);
        this.newItemPage = new NewItemPage(this);
        this.jobConfigPage = new JobConfigPage(this);
    }

    // --- Navigation & Authentication (LoginPage) ---

    public void navigate(String urlKey) {
        loginPage.openAndLoginIfNeeded(urlKey);
    }

    // --- Dashboard & Search Actions (DashboardPage) ---

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

    // --- New Item Creation & Validation Actions (NewItemPage) ---

    public void openNewItemPage() {
        newItemPage.open();
    }

    public boolean isNewItemOkEnabled() {
        return newItemPage.isOkButtonEnabled();
    }

    public String enterNewItemNameAndGetValidationError(String jobName) {
        return newItemPage.enterNameSelectFreestyleAndGetError(jobName);
    }

    // --- Job Lifecycle & Configuration Actions (JobConfigPage) ---

    public void createFreestyleJob(String jobName, String description) {
        jobConfigPage.createFreestyleJob(jobName, description);
    }

    public void createConfiguredCiJob(
            String jobName,
            String description,
            String gitUrl,
            String branch,
            String pollSchedule,
            String pomPath,
            String mavenGoals,
            String artifactPattern) {
        jobConfigPage.createConfiguredCiJob(
                jobName, description, gitUrl, branch, pollSchedule, pomPath, mavenGoals, artifactPattern
        );
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
