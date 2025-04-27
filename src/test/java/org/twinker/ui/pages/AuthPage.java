package org.twinker.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.twinker.ui.base.BasePage;

public class AuthPage extends BasePage {
    // https://www.saucedemo.com/

    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public AuthPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Log in with username: {login} and password: {password}")
    public InventoryPage logIn(String login, String password) {
        fillLogin(login);
        fillPassword(password);
        clickLoginButton();
        return new InventoryPage(driver);
    }

    @Step("Click on Login button")
    public InventoryPage clickLoginButton() {
        loginButton.click();
        return new InventoryPage(driver);
    }

    @Step("Enter username: {login}")
    public AuthPage fillLogin(String login) {
        usernameInput.sendKeys(login);
        return this;
    }

    @Step("Enter password")
    public AuthPage fillPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Log in as standard user")
    public InventoryPage standardUserLogIn() {
        return logIn("standard_user", "secret_sauce");
    }

    @Step("Get error message")
    public String getErrorMessage() {
        return errorMessage.getText();
    }
}
