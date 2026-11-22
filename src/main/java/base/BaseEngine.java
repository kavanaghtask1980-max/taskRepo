package base;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import config.ConfigManager;
import driver.DriverFactory;
import reports.ExtentManager;

public class BaseEngine {

    public WebDriver driver;
    public ExtentTest test;
    public SoftAssert softAssert;

    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor js;

    public BaseEngine() {
        softAssert = new SoftAssert();
    }

    public void openBrowser(String browserName) {
        log("Opening browser [" + browserName + "] | Headless=" + ConfigManager.getLocator("headless")
                + " | Grid=" + ConfigManager.getLocator("grid_run"));
        driver = DriverFactory.initDriver(browserName);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public void quit() {
        DriverFactory.quitDriver();
    }

    // --- Reusable Core Interaction Helpers ---

    public void jsClick(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    public void hoverAndClick(WebElement element) {
        actions.moveToElement(element).perform();
        jsClick(element);
    }

    public void hover(WebElement element) {
    actions.moveToElement(element).perform();
    }

    public void safeClick(By by) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(by)).click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                log("Stale element caught, retrying click (" + attempts + "/3)...");
            }
        }
        throw new RuntimeException("Element remained stale after 3 attempts: " + by);
    }

    public void waitForPageToLoad() {
        for (int i = 0; i < 10; i++) {
            String state = (String) js.executeScript("return document.readyState;");
            if ("complete".equals(state)) {
                log("document.readyState => " + state);
                return;
            }
            wait(1);
        }
    }

    public void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // --- Element Lookup Helpers ---

    public By getLocator(String locatorKey) {
        String value = ConfigManager.getLocator(locatorKey);
        if (value == null) {
            throw new IllegalArgumentException("Missing locator key in properties: " + locatorKey);
        }
        if (locatorKey.endsWith("_id")) return By.id(value);
        if (locatorKey.endsWith("_name")) return By.name(value);
        if (locatorKey.endsWith("_xpath")) return By.xpath(value);
        return By.cssSelector(value);
    }

    public WebElement getElement(String locatorKey) {
        By by = getLocator(locatorKey);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return driver.findElement(by);
    }

    public boolean isElementPresent(String locatorKey) {
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        boolean present = !driver.findElements(getLocator(locatorKey)).isEmpty();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        return present;
    }

    // --- Reporting & Screenshots ---

    public void log(String message) {
        if (test != null) {
            test.log(Status.INFO, message);
        }
        System.out.println(message);
    }

    public void setReport(ExtentTest test) {
        this.test = test;
    }

    public void takeScreenShot() {
        Date d = new Date();
        String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + "/" + screenshotFile));
            if (test != null) {
                test.log(Status.INFO, "Screenshot -> " + test.addScreenCaptureFromPath(
                    ExtentManager.screenshotFolderPath + "/" + screenshotFile
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reportFailure(String failureMsg, boolean stopOnFailure) {
        if (test != null) {
            test.log(Status.FAIL, failureMsg);
        }
        takeScreenShot();
        softAssert.fail(failureMsg);
        if (stopOnFailure) {
            softAssert.assertAll();
        }
    }

    public void assertAll() {
        Reporter.getCurrentTestResult().getTestContext().setAttribute("softAssert", softAssert);
        softAssert.assertAll();
    }
}
