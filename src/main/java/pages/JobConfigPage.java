package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseEngine;
import config.ConfigManager;

public class JobConfigPage {

    private final BaseEngine engine;

    public JobConfigPage(BaseEngine engine) {
        this.engine = engine;
    }

    public void createFreestyleJob(String jobName, String description) {
        engine.log("Creating Freestyle job: " + jobName);
        engine.getElement("dashboard_newItem_css").click();
        engine.getElement("newItem_nameInput_css").sendKeys(jobName);
        engine.getElement("newItem_freestyleCard_xpath").click();
        engine.getElement("newItem_okBtn_id").click();
        engine.waitForPageToLoad();

        WebElement descBox = engine.getElement("job_description_name");
        descBox.clear();
        descBox.sendKeys(description);
        engine.getElement("job_saveBtn_css").click();
        engine.waitForPageToLoad();
    }

    public void deleteCurrentJob() {
        engine.log("Deleting current Jenkins job...");
        engine.getElement("job_deleteLink_css").click();
        engine.getElement("job_confirmDeleteBtn_css").click();
        engine.waitForPageToLoad();
    }

    public void seedFreestyleJobIfMissing(String jobName, String description, DashboardPage dashboard) {
        if (dashboard.isJobListed(jobName)) {
            engine.log("Seed job already exists, skipping creation: " + jobName);
            return;
        }
        createFreestyleJob(jobName, description);
        engine.driver.get(ConfigManager.getEnv("url"));
        engine.waitForPageToLoad();
    }

    public void openBuildHistoryMenu(String buildNumber) {
        engine.log("Opening build history menu for build #" + buildNumber);
        WebElement buildContainer = engine.driver.findElement(By.cssSelector("div.app-builds-container__items"));
        List<WebElement> buildButtons = buildContainer.findElements(By.cssSelector("button.jenkins-card__reveal.jenkins-jumplist-link"));
        List<WebElement> buildLinks = buildContainer.findElements(By.cssSelector("a.app-builds-container__item__inner__link"));

        for (int i = 1; i < buildButtons.size(); i++) {
            String href = buildLinks.get(i).getAttribute("href");
            WebElement button = buildButtons.get(i);
            engine.hover(button);
            

            if (href != null && href.contains(buildNumber)) {
                engine.jsClick(button);
                engine.waitForPageToLoad();
                engine.takeScreenShot();
                break;
            }
        }
    }
}
