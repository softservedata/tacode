package com.softserve.edu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Opencart {

    public static final Logger logger = LoggerFactory.getLogger(Opencart.class); // org.slf4j.LoggerFactory
    private static final String BASE_URL = "https://demo.opencart.com/index.php";
    private static final Long IMPLICITLY_WAIT_SECONDS = 10L;
    private static final Long ONE_SECOND_DELAY = 1000L;
    private WebDriver driver;

    private void presentationSleep() {
        presentationSleep(1);
    }

    private void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY); // For Presentation ONLY
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        // WebDriverManager.firefoxdriver().setup();
        //
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");
        //driver = new ChromeDriver(options);
        driver = new ChromeDriver();
        //
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS)); // 0 by default
        driver.manage().window().maximize();
        //
        logger.info("@BeforeAll executed");
    }

    @AfterAll
    public void tear() {
        presentationSleep(4); // For Presentation ONLY
        if (driver != null) {
            driver.quit(); // close()
        }
        //
        logger.info("@AfterAll executed");
    }

    @BeforeEach
    public void setupThis() {
        driver.get(BASE_URL);
        presentationSleep(); // For Presentation ONLY
        //
        logger.info("\t@BeforeEach executed");
    }

    @AfterEach
    public void tearThis(TestInfo testInfo) {
        logger.info("\t\t\tgetTestMethod = " + testInfo.getTestMethod());
        logger.info("\t\t\tgetDisplayName = " + testInfo.getDisplayName());
        //
        logger.info("\t@AfterEach executed");
    }

    private static Stream<Arguments> currencyProvider() {
        return Stream.of(
                Arguments.of("USD", "$602.00", "mac"),
                Arguments.of("EUR", "567.02", "mac")
        );
    }


    //@Test
    @ParameterizedTest(name = "{index} => currency={0}, expectedPrice={1}, product={2}")
    @MethodSource("currencyProvider")
    public void searchMac(String currency, String expectedPrice, String product) {
        logger.info("\t\t@Test searchMac() start");
        //
        // Choose Curency
        driver.findElement(By.cssSelector("form#form-currency a.dropdown-toggle")).click();
        presentationSleep(); // For Presentation ONLY
        driver.findElement(By.cssSelector("a[href='" + currency + "']")).click();
        presentationSleep(); // For Presentation ONLY
        //
        // Steps
        // Type Search Field
        driver.findElement(By.name("search")).click();
        driver.findElement(By.name("search")).clear();
        driver.findElement(By.name("search")).sendKeys(product);
        presentationSleep(); // For Presentation ONLY
        //
        // Click Search Button
        driver.findElement(By.cssSelector("button.btn.btn-light.btn-lg")).click();
        presentationSleep(); // For Presentation ONLY
        //
        WebElement price = driver.findElement(By.xpath( "//a[text()='MacBook']/../..//div/span[@class='price-new']"));
        presentationSleep(); // For Presentation ONLY
        //
        // Scrolling by Action class
        Actions action = new Actions(driver);
        action.moveToElement(price).perform();
        presentationSleep(2); // For Presentation ONLY
        //
        // Check
        logger.info("\t\tprice.getText() = " + price.getText());
        Assertions.assertTrue(price.getText().contains(expectedPrice));
        presentationSleep(); // For Presentation ONLY
        //
        // Return to Previous State
        driver.findElement(By.cssSelector("img[alt='Your Store']")).click();
    }
}
