package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.BaseEngine;
import config.ConfigManager;

public class LoginPage {

    private final BaseEngine engine;

    public LoginPage(BaseEngine engine) {
        this.engine = engine;
    }

    public void openAndLoginIfNeeded(String urlKey) {
        engine.log("Navigating to " + urlKey);
        engine.driver.get(ConfigManager.getEnv(urlKey));
        engine.log("Page Title => " + engine.driver.getTitle());

        // In Jenkins 2.5xx, anonymous users can see Dashboard with a 'Sign in' link in the header
        engine.driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        List<WebElement> signInLinks = engine.driver.findElements(By.cssSelector("a[href*='/login']"));
        engine.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        if (!signInLinks.isEmpty() && signInLinks.get(0).isDisplayed()) {
            engine.log("Sign-in link detected on header - opening login form...");
            signInLinks.get(0).click();
            engine.waitForPageToLoad();
        }

        if (engine.isElementPresent("login_username_id")) {
            engine.log("Login screen detected - signing in...");
            engine.getElement("login_username_id").sendKeys(ConfigManager.getEnv("jenkinsUser"));
            engine.getElement("login_password_id").sendKeys(ConfigManager.getEnv("jenkinsPass"));
            engine.getElement("login_submit_css").click();
            engine.waitForPageToLoad();
        } else {
            engine.log("Already logged in.");
        }
    }
}
