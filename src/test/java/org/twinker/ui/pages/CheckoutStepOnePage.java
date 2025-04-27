package org.twinker.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.twinker.ui.base.BasePage;

public class CheckoutStepOnePage extends BasePage {
    // https://www.saucedemo.com/checkout-step-one.html

    @FindBy(css = "#first-name")
    private WebElement firstNameInput;

    @FindBy(css = "#last-name")
    private WebElement lastNameInput;

    @FindBy(css = "#postal-code")
    private WebElement postalCodeInput;

    @FindBy(css = "#continue")
    private WebElement continueButton;

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Enter first name: {text}")
    public CheckoutStepOnePage fillFirstNameInput(String text) {
        firstNameInput.sendKeys(text);
        return this;
    }

    @Step("Enter last name: {text}")
    public CheckoutStepOnePage fillLastNameInput(String text) {
        lastNameInput.sendKeys(text);
        return this;
    }

    @Step("Enter postal code: {text}")
    public CheckoutStepOnePage fillPostalCodeInput(String text) {
        postalCodeInput.sendKeys(text);
        return this;
    }

    @Step("Click on Continue button")
    public CheckoutStepTwoPage clickContinueButton() {
        continueButton.click();
        return new CheckoutStepTwoPage(driver);
    }
}
