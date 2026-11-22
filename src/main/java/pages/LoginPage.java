package pages;

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
