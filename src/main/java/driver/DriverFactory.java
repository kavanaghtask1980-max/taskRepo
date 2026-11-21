package driver;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import config.ConfigManager;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver initDriver(String browserName) {
        String targetBrowser = System.getProperty("browser", browserName);
        boolean isHeadless = "Y".equalsIgnoreCase(ConfigManager.getLocator("headless"));
        boolean isGrid = "Y".equalsIgnoreCase(ConfigManager.getLocator("grid_run"));

        WebDriver driver = isGrid
                ? createRemoteDriver(targetBrowser, isHeadless)
                : createLocalDriver(targetBrowser, isHeadless);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driverThread.set(driver);
        return getDriver();
    }

    private static WebDriver createLocalDriver(String browserName, boolean isHeadless) {
        return switch (browserName == null ? "chrome" : browserName.toLowerCase()) {
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                options.addArguments("--disable-notifications", "--remote-allow-origins=*");
                options.addArguments(isHeadless ? new String[]{"--headless=new", "--window-size=1920,1080"} : new String[]{"--start-maximized"});
                yield new EdgeDriver(options);
            }
            default -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*", "--disable-notifications", "--ignore-certificate-errors");
                options.addArguments(isHeadless ? new String[]{"--headless=new", "--window-size=1920,1080"} : new String[]{"--start-maximized"});
                yield new ChromeDriver(options);
            }
        };
    }

    private static WebDriver createRemoteDriver(String browserName, boolean isHeadless) {
        String gridUrl = ConfigManager.getEnv("grid_url") != null
                ? ConfigManager.getEnv("grid_url")
                : "http://localhost:4444";

        try {
            if ("edge".equalsIgnoreCase(browserName) || "mozilla".equalsIgnoreCase(browserName)) {
                EdgeOptions options = new EdgeOptions();
                if (isHeadless) options.addArguments("--headless=new", "--window-size=1920,1080");
                return new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
            }

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
            if (isHeadless) options.addArguments("--headless=new", "--window-size=1920,1080");
            return new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize RemoteWebDriver at: " + gridUrl, e);
        }
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
        }
    }
}
