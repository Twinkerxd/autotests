package org.twinker.ui.pages;

import io.qameta.allure.Step;
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

    @Step("Add item to cart by name: {itemName}")
    public InventoryPage addItemToCart(String itemName) {
        String generatedXpath = "//*[text()='" + itemName + "']/parent::a/parent::div/following-sibling::*/button";
        driver.findElement(By.xpath(generatedXpath)).click();
        return this;
    }

    @Step("Click on cart icon")
    public CartPage clickCartIcon() {
        cartIcon.click();
        return new CartPage(driver);
    }

    @Step("Get shopping cart badge")
    public WebElement getShoppingCartBadge() {
        return shoppingCartBadge;
    }

    @Step("Add random item to cart")
    public InventoryPage addRandomAddItemToCart() {
        Random random = new Random();
        addToCartButtons.get(random.nextInt(addToCartButtons.size())).click();
        return this;
    }

    @Step("Click on sort dropdown")
    public InventoryPage clickSortDropdown() {
        sortDropdown.click();
        return this;
    }

    @Step("Select sort option: {sortOption.label}")
    public InventoryPage selectSortOptionByValue(ProductSortOption sortOption) {
        Select select = new Select(sortDropdown);
        select.selectByValue(sortOption.getValue());
        return this;
    }

    public enum ProductSortOption {
        NAME_ASC("az", "Name: A to Z"),
        NAME_DESC("za", "Name: Z to A"),
        PRICE_LOWER_HIGH("lohi", "Price: Low to High"),
        PRICE_HIGH_LOWER("hilo", "Price: High to Low");

        private final String value;
        private final String label;

        ProductSortOption(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    @Step("Get list of product prices")
    public List<Double> getListOfPrices() {
        return listOfPrices.stream()
                .map(WebElement::getText)
                .map(price -> price.replace("$", ""))
                .map(Double::parseDouble)
                .toList();
    }

    @Step("Move mouse cursor to first product name")
    public InventoryPage moveCursorToFirstItemName() {
        Actions actions = new Actions(driver);
        actions.moveToElement(firstItemName).perform();
        return this;
    }

    @Step("Get color of first product name")
    public String getFirstItemColor() {
        return firstItemName.getCssValue("color");
    }
}
