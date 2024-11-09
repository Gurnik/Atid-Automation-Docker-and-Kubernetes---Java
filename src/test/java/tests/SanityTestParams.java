package tests;

import com.google.common.util.concurrent.Uninterruptibles;
import infra.DriverManager;
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

public class SanityTestParams {
    private WebDriver driver;

    @Parameters({"browser"})
    @BeforeClass
    public void setup(String browser) throws MalformedURLException {
        driver = new DriverManager().getDriver(browser);
        driver.manage().window().maximize();
        Uninterruptibles.sleepUninterruptibly(7, TimeUnit.SECONDS);
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
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    public void selectAllItems() {
        int numItems = driver.findElements(By.className("inventory_item_name")).size();
        for (int i = 0; i < numItems; i++) {
            driver.findElements(By.className("inventory_item_name")).get(i).click();
            driver.findElement(By.cssSelector("button[class='btn btn_primary btn_small btn_inventory")).click();
            driver.findElement(By.id("back-to-products")).click();
        }
    }

    public void verifyNumberOfItemsCart(String expected) {
        assertEquals(driver.findElement(By.className("shopping_cart_badge")).getText(), expected);
    }
}
