package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import base.BaseEngine;
import config.ConfigManager;

public class DashboardPage {

    private final BaseEngine engine;

    public DashboardPage(BaseEngine engine) {
        this.engine = engine;
    }

    public boolean isMenuLinkDisplayed(String locatorKey) {
        return engine.isElementPresent(locatorKey) && engine.getElement(locatorKey).isDisplayed();
    }

    public void search(String query) {
        engine.log("Searching Jenkins for: " + query);
        engine.getElement("dashboard_searchBtn_id").click();
        WebElement searchInput = engine.getElement("dashboard_searchInput_css");
        searchInput.sendKeys(query);
        engine.wait(1);
        searchInput.sendKeys(Keys.ENTER);
        engine.waitForPageToLoad();
    }

    public boolean isJobListed(String jobName) {
        engine.driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        boolean found = !engine.driver.findElements(
            By.xpath("//tr[@id='job_" + jobName + "']")
        ).isEmpty();
        engine.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        return found;
    }

    public void openJobContextMenu(String jobName) {
        engine.log("Opening dashboard dropdown menu for job: " + jobName);
        WebElement jobLink = engine.driver.findElement(By.xpath("//a[@href='job/" + jobName + "/']"));
        WebElement chevron = engine.driver.findElement(
            By.xpath("//tr[@id='job_" + jobName + "']//button[contains(@class,'jenkins-menu-dropdown-chevron')]")
        );
        engine.hover(jobLink);
        chevron.click();
    }

    public void selectContextMenuOption(String menuOption) {
        WebElement menu = engine.getElement("dashboard_contextMenu_css");
        List<WebElement> items = menu.findElements(By.className("jenkins-dropdown__item"));

        for (WebElement item : items) {
            String optionText = item.getText();
            engine.hover(item);

            if (optionText.contains(menuOption)) {
                engine.log("Selected context menu option: " + optionText);
                engine.jsClick(item);
                engine.waitForPageToLoad();
                break;
            }
        }
    }
}
