package org.twinker.ui.core;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.twinker.ui.pages.AuthPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    //beforeAll -> docker, kafka


    @BeforeEach
    @Step("Initialize WebDriver and browser settings")
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--guest");

        String remoteUrl = System.getProperty("REMOTE_URL");


        if (remoteUrl != null) {
            System.out.println("Running REMOTELY on: " + remoteUrl);
            options.setCapability("browserName", "chrome");
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } else {
            System.out.println("Running LOCALLY");
            driver = new ChromeDriver(options);
        }


        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        wait = new WebDriverWait(driver, Duration.ofSeconds(4));
    }


    @AfterEach
    @Step("Take screenshot, close browser and clean up WebDriver")
    public void tearDown() {
        saveScreenshot("screen name");

        if (driver != null) {
            driver.quit();
        }
    }

    @Step("Navigating to the main page")
    public AuthPage openMainPage() {
        driver.get("https://www.saucedemo.com/");
        return new AuthPage(driver);
    }

    public void saveScreenshot(String name) {
        Allure.getLifecycle().addAttachment(name, "image/png", "png",
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
    }
}
