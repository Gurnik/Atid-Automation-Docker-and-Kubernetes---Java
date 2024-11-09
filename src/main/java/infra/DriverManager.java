package infra;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverManager {
    private WebDriver driver;

    public WebDriver getDriver(String browser) throws MalformedURLException {
        String type = System.getProperty("EXEC");
        if(type.equalsIgnoreCase("local")) {
            driver = getLocalDriver(browser);
        }
        else {
            driver = getRemoteDriver(browser);
        }
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver getLocalDriver(String browser) {
        return getLocalBrowser(browser);
    }

    public WebDriver getLocalBrowser(String browser) {
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + browser.toLowerCase());
        }
        return driver;
    }

    public WebDriver getRemoteDriver(String browser) throws MalformedURLException {
        driver = new RemoteWebDriver(new URL("http://localhost:4444"), getRemoteBrowser(browser));
        return driver;
    }

    public MutableCapabilities getRemoteBrowser(String browser) {
        MutableCapabilities option;
        switch (browser.toLowerCase()) {
            case "firefox":
                option = new FirefoxOptions();
                break;
            case "chrome":
                option = new ChromeOptions();
                break;
            case "edge":
                option = new EdgeOptions();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + browser.toLowerCase());
        }
        option.setCapability("se:recordVideo", true); // for recording videos at Dynamic Grid
        return option;
    }
}
