package tests;

import com.google.common.util.concurrent.Uninterruptibles;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

public class SanityTestHubNodes {
    private WebDriver driver;

    @BeforeClass
    public void setup() throws MalformedURLException {
        // Chrome
        WebDriverManager.chromedriver().setup();
        ChromeOptions optionChrome = new ChromeOptions();

        // Firefox
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions optionFF = new FirefoxOptions();

        // Edge
        WebDriverManager.edgedriver().setup();
        EdgeOptions optionEdge = new EdgeOptions();

        driver = new RemoteWebDriver(new URL("http://localhost:4444"), optionFF);
        // driver.manage().window().setSize(new Dimension(1920, 1080));
        // driver.manage().window().setPosition(new Point(620, 0));
        driver.manage().window().maximize();
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        // driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        login("standard_user", "secret_sauce");
    }

    @Test
    public void test01_count_items() {
        selectAllItems();
        verifyNumberOfItemsCart("6");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    public void login(String username, String password) {
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.findElement(By.id("login-button")).click();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    public void selectAllItems() {
        int numItems = driver.findElements(By.className("inventory_item_name")).size();
        for (int i = 0; i < numItems; i++) {
            driver.findElements(By.className("inventory_item_name")).get(i).click();
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            driver.findElement(By.cssSelector("button[class='btn btn_primary btn_small btn_inventory")).click();
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            driver.findElement(By.id("back-to-products")).click();
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        }
    }

    public void verifyNumberOfItemsCart(String expected) {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        assertEquals(driver.findElement(By.className("shopping_cart_badge")).getText(), expected);
    }
}
