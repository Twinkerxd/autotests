package org.twinker.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.twinker.ui.core.BasePage;

public class CheckoutCompletePage extends BasePage {
    // https://www.saucedemo.com/checkout-complete.html

    @FindBy(css = "#back-to-products")
    private WebElement backHomeButton;

    @FindBy(css = "[data-test='complete-header']")
    private WebElement completeHeaderText;

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Click on 'Back Home' button")
    public void clickBackHomeButton() {
        backHomeButton.click();
    }

    @Step("Get checkout completion header text")
    public String getCompleteHeaderText() {
        return completeHeaderText.getText();
    }
}
