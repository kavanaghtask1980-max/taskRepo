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

        public void createConfiguredCiJob(
            String jobName,
            String description,
            String gitUrl,
            String branch,
            String pollSchedule,
            String pomPath,
            String mavenGoals,
            String artifactPattern) {

        engine.log("Creating fully configured CI Freestyle job: " + jobName);
        engine.getElement("dashboard_newItem_css").click();
        engine.getElement("newItem_nameInput_css").sendKeys(jobName);
        engine.getElement("newItem_freestyleCard_xpath").click();
        engine.getElement("newItem_okBtn_id").click();
        engine.waitForPageToLoad();

        // 1. General Description
        WebElement descBox = engine.getElement("job_description_name");
        descBox.clear();
        descBox.sendKeys(description);

             // 2. Source Code Management -> Git
     WebElement gitRadio = engine.driver.findElement(By.xpath("//label[normalize-space()='Git']"));
     engine.jsClick(gitRadio);
     engine.wait(1);

     WebElement repoInput = engine.driver.findElements(By.name("_.url")).stream()
         .filter(WebElement::isDisplayed)
         .findFirst()
         .orElseThrow(() -> new RuntimeException("Visible Git Repository URL input not found"));
     repoInput.clear();
     repoInput.sendKeys(gitUrl);

     WebElement branchInput = engine.driver.findElements(By.name("_.name")).stream()
         .filter(WebElement::isDisplayed)
         .findFirst()
         .orElseThrow(() -> new RuntimeException("Visible Git Branch Specifier input not found"));
     branchInput.clear();
     branchInput.sendKeys(branch);

        // 3. Build Triggers -> Poll SCM
        WebElement pollCheckbox = engine.driver.findElement(By.xpath("//label[normalize-space()='Poll SCM']"));
        engine.jsClick(pollCheckbox);
        WebElement scheduleBox = engine.driver.findElement(By.name("_.scmpoll_spec"));
        scheduleBox.clear();
        scheduleBox.sendKeys(pollSchedule);

        // 4. Build Steps -> Invoke top-level Maven targets
        WebElement addBuildStepBtn = engine.driver.findElement(
            By.xpath("//button[contains(normalize-space(),'Add build step')]")
        );
        engine.jsClick(addBuildStepBtn);
        WebElement mavenOption = engine.driver.findElement(
            By.xpath("//button[contains(normalize-space(),'Invoke top-level Maven targets')]")
        );
        engine.jsClick(mavenOption);

        WebElement goalsInput = engine.driver.findElement(By.name("_.targets"));
        goalsInput.clear();
        goalsInput.sendKeys(mavenGoals);

        WebElement advancedBtn = engine.driver.findElement(
            By.xpath("//div[contains(@descriptorid,'Maven')]//button[contains(normalize-space(),'Advanced')]")
        );
        engine.jsClick(advancedBtn);

        WebElement pomInput = engine.driver.findElement(By.name("_.pom"));
        pomInput.clear();
        pomInput.sendKeys(pomPath);

        // 5. Post-build Actions -> Archive the artifacts
        WebElement addPostBuildBtn = engine.driver.findElement(
            By.xpath("//button[contains(normalize-space(),'Add post-build action')]")
        );
        engine.jsClick(addPostBuildBtn);
        WebElement archiveOption = engine.driver.findElement(
            By.xpath("//button[contains(normalize-space(),'Archive the artifacts')]")
        );
        engine.jsClick(archiveOption);

        WebElement artifactsInput = engine.driver.findElement(By.name("_.artifacts"));
        artifactsInput.clear();
        artifactsInput.sendKeys(artifactPattern);

        // 6. Save Job
        engine.getElement("job_saveBtn_css").click();
        engine.waitForPageToLoad();
    }

}