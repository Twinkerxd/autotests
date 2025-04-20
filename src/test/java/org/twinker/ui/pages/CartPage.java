package org.twinker.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.twinker.ui.core.BasePage;

import java.util.List;

public class CartPage extends BasePage {
    // https://www.saucedemo.com/cart.html

    @FindBy(css = "#checkout")
    private WebElement checkoutButton;

    @FindBy(css = "[id^='remove']")
    private WebElement removeButton;

    @FindBy(css = "[data-test='inventory-item']")
    private List<WebElement> inventoryItems;

    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public CheckoutStepOnePage clickCheckoutButton() {
        checkoutButton.click();
        return new CheckoutStepOnePage(driver);
    }

    public CartPage clickRemoveButton() {
        removeButton.click();
        return this;
    }

    public int getItemsCount() {
        return inventoryItems.size();
    }
}
