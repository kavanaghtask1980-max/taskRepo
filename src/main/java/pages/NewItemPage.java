package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseEngine;
import config.ConfigManager;

public class NewItemPage {

    private final BaseEngine engine;

    public NewItemPage(BaseEngine engine) {
        this.engine = engine;
    }

    public void open() {
        engine.driver.get(ConfigManager.getEnv("url") + "view/all/newJob");
        engine.waitForPageToLoad();
    }

    public boolean isOkButtonEnabled() {
        return engine.getElement("newItem_okBtn_id").isEnabled();
    }

        public String enterNameSelectFreestyleAndGetError(String jobName) {
        WebElement input = engine.getElement("newItem_nameInput_css");
        input.clear();
        input.sendKeys(jobName);
        input.sendKeys(org.openqa.selenium.Keys.TAB);
        engine.getElement("newItem_freestyleCard_xpath").click();

        By errorLocator = By.xpath(ConfigManager.getLocator("newItem_validationError_xpath"));
        WebElement errorBanner = new WebDriverWait(engine.driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(errorLocator));
        return errorBanner.getText();
    }

}
