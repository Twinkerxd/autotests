package org.twinker.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.twinker.ui.base.BasePage;

public class CheckoutStepTwoPage extends BasePage {
    // https://www.saucedemo.com/checkout-step-two.html

    @FindBy(css = "#finish")
    private WebElement finishButton;

    @FindBy(css = "#cancel")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='total-label']")
    private WebElement totalLabel;

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Click on Finish button")
    public CheckoutCompletePage clickFinishButton() {
        finishButton.click();
        return new CheckoutCompletePage(driver);
    }

    @Step("Click on Cancel button")
    public void clickCancelButton() {
        cancelButton.click();
    }

    @Step("Get total amount label text")
    public String getTotalLabelText() {
        return totalLabel.getText();
    }
}
