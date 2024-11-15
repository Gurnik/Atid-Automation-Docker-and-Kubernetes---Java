package tests;

import com.google.common.util.concurrent.Uninterruptibles;
import infra.DockerManager;
import infra.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

public class SanityTestParamsGridAutoSetup {
    private WebDriver driver;
    DockerManager dockerManager = new DockerManager();

    @BeforeSuite
    public void startDocker() {
        dockerManager.actionDocker("START");
    }

    @AfterSuite
    public void stopDocker() {
        dockerManager.actionDocker("STOP");
    }

    @Parameters({"browser"})
    @BeforeClass
    public void setup(String browser) throws MalformedURLException {
        driver = new DriverManager().getDriver(browser);
        driver.manage().window().maximize();
        Uninterruptibles.sleepUninterruptibly(7, TimeUnit.SECONDS);
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
            driver.findElement(By.cssSelector("button[class='btn btn_primary btn_small btn_inventory']")).click();
            driver.findElement(By.id("back-to-products")).click();
        }
    }

    public void verifyNumberOfItemsCart(String expected) {
        assertEquals(driver.findElement(By.className("shopping_cart_badge")).getText(), expected);
    }
}
