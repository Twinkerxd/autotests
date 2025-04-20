package org.twinker.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.twinker.ui.core.BasePage;

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

    public CheckoutCompletePage clickFinishButton() {
        finishButton.click();
        return new CheckoutCompletePage(driver);
    }
}
