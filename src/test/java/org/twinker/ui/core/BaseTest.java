package org.twinker.ui.core;

import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
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

    @Step("Initialize WebDriver and browser settings")
    @BeforeEach
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--guest");

        boolean isRemoteRun = true;

        if (isRemoteRun) {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName("chrome");
            driver = new RemoteWebDriver(
                    new URL("https://user1:1234@selenoid.autotests.cloud/wd/hub"),
                    capabilities
            );
        } else {
            driver = new ChromeDriver(options);
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        wait = new WebDriverWait(driver, Duration.ofSeconds(4));
    }

    @Step("Close browser and clean up WebDriver")
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Step("Navigating to the main page")
    public AuthPage openMainPage() {
        driver.get("https://www.saucedemo.com/");
        return new AuthPage(driver);
    }

}
