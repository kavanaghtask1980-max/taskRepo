package keywords;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import base.BaseEngine;
import config.ConfigManager;

public class ApplicationKeywords extends BaseEngine {

    public ApplicationKeywords() {
        super();
    }

    // --- Navigation & Authentication ---

    public void navigate(String urlKey) {
        log("Navigating to " + urlKey);
        driver.get(ConfigManager.getEnv(urlKey));
        log("Page Title => " + driver.getTitle());

        if (isElementPresent("login_username_id")) {
            log("Login screen detected - signing in...");
            getElement("login_username_id").sendKeys(ConfigManager.getEnv("jenkinsUser"));
            getElement("login_password_id").sendKeys(ConfigManager.getEnv("jenkinsPass"));
            getElement("login_submit_css").click();
            waitForPageToLoad();
        } else {
            log("Already logged in.");
        }
    }

    // --- Dashboard & Search Actions ---

    public String getPageTitle() {
        return driver.getTitle();
    }

    
    public boolean isMenuLinkDisplayed(String locatorKey) {
        return isElementPresent(locatorKey) && getElement(locatorKey).isDisplayed();
    }

    public void searchJenkins(String query) {
        log("Searching Jenkins for: " + query);
        getElement("dashboard_searchBtn_id").click();
        WebElement searchInput = getElement("dashboard_searchInput_css");
        searchInput.sendKeys(query);
        wait(1);
        searchInput.sendKeys(Keys.ENTER);
        waitForPageToLoad();
    }

    // --- Job Lifecycle Actions ---

    public void createFreestyleJob(String jobName, String description) {
        log("Creating Freestyle job: " + jobName);
        getElement("dashboard_newItem_css").click();
        getElement("newItem_nameInput_css").sendKeys(jobName);
        getElement("newItem_freestyleCard_xpath").click();
        getElement("newItem_okBtn_id").click();
        waitForPageToLoad();

        WebElement descBox = getElement("job_description_name");
        descBox.clear();
        descBox.sendKeys(description);
        getElement("job_saveBtn_css").click();
        waitForPageToLoad();
    }

    public void deleteCurrentJob() {
        log("Deleting current Jenkins job...");
        getElement("job_deleteLink_css").click();
        getElement("job_confirmDeleteBtn_css").click();
        waitForPageToLoad();
    }

    public boolean isJobListedOnDashboard(String jobName) {
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        boolean found = !driver.findElements(
            By.xpath("//tr[@id='job_" + jobName + "']")
        ).isEmpty();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        return found;
    }

    // --- Dashboard & Build History Menu Actions ---

    public void openDashboardJobMenu(String jobName) {
        log("Opening dashboard dropdown menu for job: " + jobName);
        WebElement jobLink = driver.findElement(By.xpath("//a[@href='job/" + jobName + "/']"));
        WebElement chevron = driver.findElement(
            By.xpath("//tr[@id='job_" + jobName + "']//button[contains(@class,'jenkins-menu-dropdown-chevron')]")
        );
        actions.moveToElement(jobLink).perform();
        chevron.click();
    }

    public void selectContextMenuOption(String menuOption) {
        WebElement menu = getElement("dashboard_contextMenu_css");
        List<WebElement> items = menu.findElements(By.className("jenkins-dropdown__item"));

        for (WebElement item : items) {
            String optionText = item.getText();
            actions.moveToElement(item).perform();

            if (optionText.contains(menuOption)) {
                log("Selected context menu option: " + optionText);
                jsClick(item);
                waitForPageToLoad();
                break;
            }
        }
    }

    public void seedFreestyleJob(String jobName, String description) {
    if (isJobListedOnDashboard(jobName)) {
        log("Seed job already exists, skipping creation: " + jobName);
        return;
    }
    createFreestyleJob(jobName, description);
    driver.get(ConfigManager.getEnv("url")); // Return to dashboard for next seed row
    waitForPageToLoad();
}

    public void openBuildHistoryMenu(String buildNumber) {
        log("Opening build history menu for build #" + buildNumber);
        WebElement buildContainer = driver.findElement(By.cssSelector("div.app-builds-container__items"));
        List<WebElement> buildButtons = buildContainer.findElements(By.cssSelector("button.jenkins-card__reveal.jenkins-jumplist-link"));
        List<WebElement> buildLinks = buildContainer.findElements(By.cssSelector("a.app-builds-container__item__inner__link"));

        for (int i = 1; i < buildButtons.size(); i++) {
            String href = buildLinks.get(i).getAttribute("href");
            WebElement button = buildButtons.get(i);
            actions.moveToElement(button).perform();

            if (href != null && href.contains(buildNumber)) {
                jsClick(button);
                waitForPageToLoad();
                takeScreenShot();
                break;
            }
        }
    }

    // <---->

        public void resetJenkinsToVanillaState(String protectedJobName) {
        log("Resetting Jenkins dashboard to vanilla state (preserving: " + protectedJobName + ")");
        driver.get(ConfigManager.getEnv("url"));
        waitForPageToLoad();

        List<WebElement> jobRows = driver.findElements(By.cssSelector("tr[id^='job_']"));
        List<String> jobsToDelete = new java.util.ArrayList<>();

        for (WebElement row : jobRows) {
            String rowId = row.getAttribute("id");
            if (rowId != null) {
                String jobName = rowId.replace("job_", "");
                if (!jobName.equalsIgnoreCase(protectedJobName)) {
                    jobsToDelete.add(jobName);
                }
            }
        }

        for (String jobName : jobsToDelete) {
            log("Cleaning up job: " + jobName);
            driver.get(ConfigManager.getEnv("url") + "job/" + jobName + "/");
            deleteCurrentJob();
        }
        log("Jenkins reset complete. Removed " + jobsToDelete.size() + " jobs.");
    }

}
