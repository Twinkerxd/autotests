package org.twinker.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.twinker.ui.core.BasePage;

import java.util.List;
import java.util.Random;

public class InventoryPage extends BasePage {
    // https://www.saucedemo.com/inventory.html

    @FindBy(css = "[data-test='shopping-cart-link']")
    private WebElement cartIcon;

    @FindBy(css = "[data-test='shopping-cart-badge']")
    private WebElement shoppingCartBadge;

    @FindBy(css = "[data-test^='add-to-cart']")
    private List<WebElement> addToCartButtons;

    @FindBy(css = "[data-test='product-sort-container']")
    private WebElement sortDropdown;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> listOfPrices;

    @FindBy(css = "[data-test='inventory-item-name']")
    private WebElement firstItemName;

    public InventoryPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public InventoryPage addItemToCart(String itemName) {
        String generatedXpath = "//*[text()='" + itemName + "']/parent::a/parent::div/following-sibling::*/button";
        driver.findElement(By.xpath(generatedXpath)).click();
        return this;
    }

    public CartPage clickCartIcon() {
        cartIcon.click();
        return new CartPage(driver);
    }

    public WebElement getShoppingCartBadge() {
        return shoppingCartBadge;
    }

    public InventoryPage addRandomAddItemToCart() {
        Random random = new Random();
        addToCartButtons.get(random.nextInt(addToCartButtons.size())).click();
        return this;
    }

    public InventoryPage clickSortDropdown() {
        sortDropdown.click();
        return this;
    }

    public InventoryPage selectSortOptionByValue(ProductSortOption sortOption) {
        Select select = new Select(sortDropdown);
        select.selectByValue(sortOption.getValue());
        return this;
    }

    public enum ProductSortOption {
        NAME_ASC("az"),
        NAME_DESC("za"),
        PRICE_LOWER_HIGH("lohi"),
        PRICE_HIGH_LOWER("hilo");

        private final String value;

        ProductSortOption(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public List<Double> getListOfPrices() {
        List<Double> priceWithDollar = listOfPrices.stream().map(WebElement::getText)
                .map(price -> price.replace("$", ""))
                .map(Double::parseDouble).toList();

        return priceWithDollar;
    }

    public InventoryPage moveCursorToFirstItemName() {
        Actions actions = new Actions(driver);
        actions.moveToElement(firstItemName).perform();
        return this;
    }

    public String getFirstItemColor() {
        return firstItemName.getCssValue("color");
    }
}
