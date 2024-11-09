package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

public class SanityTest {
    private WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().window().setPosition(new Point(620, 0));
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
