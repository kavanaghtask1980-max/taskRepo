package keywords;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import reports.ExtentManager;

public class ApplicationKeywords {

    

    public WebDriver driver;
    public Properties prop;
    public Properties envProp;
    public ExtentTest test;
    public SoftAssert softAssert;

    public ApplicationKeywords() {

        String path = System.getProperty("user.dir") + "//src//test//resources//env.properties";
        prop = new Properties();
        envProp = new Properties();
        try {
            FileInputStream fs = new FileInputStream(path);
            prop.load(fs);
            String env = prop.getProperty("env") + ".properties";
            path = System.getProperty("user.dir") + "//src//test//resources//" + env;
            fs = new FileInputStream(path);
            envProp.load(fs);
        } catch (Exception e) {

            e.printStackTrace();
        }

        softAssert = new SoftAssert();
    }

        public void hpSelectBuildJobPopMenuOne(String menuOption) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);

        WebElement hpSelectBuildPopJobMenu = getElement("hpSelectBuildJobPopMenu_css");
        List<WebElement> elements = hpSelectBuildPopJobMenu.findElements(By.className("jenkins-dropdown__item"));

        log("Number of menu items:" + elements.size());

        for (int i = 0; i < elements.size(); i++) {
            String hpSelectBuildPopJobMenuOptions = elements.get(i).getText();
            log("Menu choice:" + hpSelectBuildPopJobMenuOptions);
            
                WebElement e = elements.get(i);
                actions.moveToElement(e).build().perform();

            if (hpSelectBuildPopJobMenuOptions.contains(menuOption)) {
                log("Menu choice selected: " + hpSelectBuildPopJobMenuOptions);
                js.executeScript("javascript:arguments[0].click();", e);
                waitForPageToLoad();
                WebElement homeJenkins = driver.findElement(By.cssSelector("span.jenkins-mobile-hide"));
                js.executeScript("javascript:arguments[0].click();", homeJenkins);
                waitForPageToLoad();
                break;
            }

        }

    }

    public void hpSelectBuildJobPopMenu(String menuOption) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);

        WebElement hpSelectBuildPopJobMenu = getElement("hpSelectBuildJobPopMenu_css");
        List<WebElement> elements = hpSelectBuildPopJobMenu.findElements(By.className("jenkins-dropdown__item"));

        log("Number of menu items:" + elements.size());

        for (int i = 0; i < elements.size(); i++) {
            String hpSelectBuildPopJobMenuOptions = elements.get(i).getText();
            log("Menu choice:" + hpSelectBuildPopJobMenuOptions);
            
                WebElement e = elements.get(i);
                actions.moveToElement(e).build().perform();

            if (hpSelectBuildPopJobMenuOptions.contains(menuOption)) {
                log("Menu choice selected: " + hpSelectBuildPopJobMenuOptions);
                js.executeScript("javascript:arguments[0].click();", e);
                waitForPageToLoad();
                takeScreenShot();
                break;
            }

        }

    }

    public void buildNumberMenu1(String buildNumber) {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);

        WebElement buildNumberNav = driver.findElement(By.cssSelector("div.app-builds-container__items"));
        List<WebElement> buildMenuButtons = buildNumberNav.findElements(By.cssSelector("button.jenkins-card__reveal.jenkins-jumplist-link"));

        List<WebElement> buildInnerText = buildNumberNav.findElements(By.cssSelector("a.app-builds-container__item__inner__link"));

        int numberOfBuildButtons = buildMenuButtons.size();
        log("builds available: " + numberOfBuildButtons);

        for (int i = 1; i < buildMenuButtons.size(); i++) {

            String buildInnerTextRecordAtt = buildInnerText.get(i).getAttribute("href");
            WebElement buildNumberButton = buildMenuButtons.get(i);
            
            log("Build number / Details: " + buildInnerTextRecordAtt);
           
            actions.moveToElement(buildNumberButton).build().perform();
            
            if(buildInnerTextRecordAtt.contains(buildNumber)){
                js.executeScript("javascript:arguments[0].click();", buildNumberButton);
                waitForPageToLoad();
            }break;
        }
    }

        public void buildNumberMenu(String buildNumber) {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);

        WebElement buildNumberNav = driver.findElement(By.cssSelector("div.app-builds-container__items"));
        List<WebElement> buildMenuButtons = buildNumberNav.findElements(By.cssSelector("button.jenkins-card__reveal.jenkins-jumplist-link"));

        List<WebElement> buildInnerText = buildNumberNav.findElements(By.cssSelector("a.app-builds-container__item__inner__link"));

        int numberOfBuildButtons = buildMenuButtons.size();
        log("builds available: " + numberOfBuildButtons);

        for (int i = 1; i < buildMenuButtons.size(); i++) {

            String buildInnerTextRecordAtt = buildInnerText.get(i).getAttribute("href");
            WebElement buildNumberButton = buildMenuButtons.get(i);
            
            log("Build number / Details: " + buildInnerTextRecordAtt);
            log("loop number: " + i);

            actions.moveToElement(buildNumberButton).build().perform();
            
            if(buildInnerTextRecordAtt.contains(buildNumber)){
                js.executeScript("javascript:arguments[0].click();", buildNumberButton);
                waitForPageToLoad();
                takeScreenShot();
               break;
             }
        }
    }

    public void wait(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void waitForPageToLoad() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int i = 0;

        while (i != 10) {
            String state = (String) js.executeScript("javascript:return document.readyState;");

            if (state.equals("complete")) {
                log("return document.readyState => " + state);

                break;
            } else //.........wait system
                log("Waiting else => " + state);
            {
                wait(2);
                
                 log("Waiting after => " + state);
            }
            log("Going around waitForPageToLoad");
            i++;
        }
        // != undefined && jQuery.active == 0;");
        // log("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        // wait(2);// wait of 2 sec between page status and jquery
        // //check for jquery status
        // i=0;
        // while(i!=10){

        // 	Boolean result= (Boolean) js.executeScript("javascript:return window.jQuery;");
        // 	log(result);
        // 	if(result){
        //         log("result => " + result);
        //         break;}
        // 	else
        // 		 wait(2);
        //          log("wait => " + i);
        // 	 i++;
        // 	}
    }

    public void openBrowser(String browserName) {

        log("Opening the browser " + browserName);

        if (browserName.equalsIgnoreCase("Chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\kavan\\Automation_Task\\demo\\chromedriver.exe");
            ChromeOptions chOptions = new ChromeOptions();
            chOptions.addArguments("--remote-allow-origins=*");
            chOptions.addArguments("--disable-notifications");
            chOptions.addArguments("--start-maximized");
            chOptions.addArguments("--ignore-certificate-errors");
            driver = new ChromeDriver(chOptions);

        } else if (browserName.equalsIgnoreCase("Edge")) {
            System.setProperty("webdriver.edge.driver", "C:\\Users\\kavan\\Automation_Task\\demo\\msedgedriver.exe");
            //  System.setProperty(EdgeDriverService.EDGE_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            EdgeOptions options = new EdgeOptions();
            options.setPageLoadStrategy(PageLoadStrategy.EAGER);
            //options.setBinary(new File(""));
            options.addArguments("--disable-notifications");
            options.addArguments("--start-maximized");
            options.addArguments("--remote-allow-origins=*");
            driver = new EdgeDriver(options);

        }
        // dynamic wait- not pause
        // global time out- all driver.findelement
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    public void navigate(String url) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        log("Navigating to " + url);
        driver.get(envProp.getProperty(url));
        String title = driver.getTitle();
        log("Page Title => " + title);
        WebElement userName = driver.findElement(By.cssSelector("input#j_username"));
        WebElement passWord = driver.findElement(By.cssSelector("input#j_password"));
        WebElement signInBut = driver.findElement(By.cssSelector("button.jenkins-button.jenkins-button--primary"));
        userName.sendKeys("kavanaghtom1980");
        passWord.sendKeys("Desire97!");
        signInBut.click();
        waitForPageToLoad();
        log("Navigating to " + url);
    }

        public void buildNumberSel() {
        Actions actions = new Actions(driver);
        WebElement buildJobName = driver.findElement(By.xpath("//a[@href='job/test001/']"));
        actions.moveToElement(buildJobName).build().perform();
        actions.moveToElement(buildJobName, 25, 0).click().perform();
        
    }



    public void quit() {
        String title = driver.getTitle();
        log("Closing browser => " + title);
        driver.quit();
    }

    public void click(String locatorKey) {
        log("Clicking on " + locatorKey);
        getElement(locatorKey).click();
    }

    public void type(String locatorKey, String data) {
        log("Typing into locator => " + locatorKey + " with => " + data);
        getElement(locatorKey).sendKeys(data);
    }

    public void select(String locater, String data) {

    }

    public void getText(String locatorKey) {
        String text = getElement(locatorKey).getText();
        log(text);
    }

    public void setReport(ExtentTest test) {
        this.test = test;
    }

    public void log(String msg) {
        test.log(Status.INFO, msg);
    }

    public void reportFailure(String failureMsg, boolean stopOnFailure) {
        takeScreenShot();
        test.log(Status.FAIL, failureMsg);
        softAssert.fail(failureMsg);
        if (stopOnFailure) {
            Reporter.getCurrentTestResult().getTestContext().setAttribute("criticalFailure", "Y");
            assertAll();
        }
    }

    public void assertAll() {
        softAssert.assertAll();
    }

    public void takeScreenShot() {
        // fileName of the screenshot
        Date d = new Date();
        String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
        // take screenshot
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            // get the dynamic folder name
            FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + "//" + screenshotFile));
            //put screenshot file in reports
            test.log(Status.INFO, "Screenshot-> " + test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath + "//" + screenshotFile));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public int getRowNumWithCellData(String tableLocator, String data) {

        WebElement table = getElement(tableLocator);
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        for (int rNum = 0; rNum < rows.size(); rNum++) {
            WebElement row = rows.get(rNum);
            List<WebElement> cells = row.findElements(By.tagName("td"));
            for (int cNum = 0; cNum < cells.size(); cNum++) {
                WebElement cell = cells.get(cNum);
                log("Text " + cell.getText());
                if (!cell.getText().trim().equals("")) {
                    if (data.startsWith(cell.getText())) {
                        return (rNum + 1);
                    }
                }
            }
        }

        return -1; // data is not found
    }

    public WebElement getElement(String locatorKey) {
        //  check the presence

        //lkgetLocator(locaterKey);
        if (!isElementPresent(locatorKey)) {
            // report failure
            log("Element not present "+locatorKey);
        }
        //  check the visibility
        if (!isElementVisible(locatorKey)) {
            log("Element not visible "+locatorKey);
        }

        WebElement e = driver.findElement(getLocator(locatorKey));

        return e;
    }

    public boolean isElementPresent(String locatorKey) {
        log("Checking presence of " + locatorKey);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locatorKey)));

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // true - visible
    // false - not visible
    public boolean isElementVisible(String locatorKey) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locatorKey)));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public By getLocator(String locatorKey) {
        By by = null;
        if (locatorKey.endsWith("_id")) {
            by = By.id(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_xpath")) {
            by = By.xpath(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_css")) {
            by = By.cssSelector(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_name")) {
            by = By.name(prop.getProperty(locatorKey));
        }

        return by;

    }

}
