package org.twinker.tests.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.twinker.ui.core.BaseTest;
import org.twinker.ui.pages.AuthPage;
import org.twinker.ui.pages.CartPage;
import org.twinker.ui.pages.InventoryPage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.twinker.ui.pages.InventoryPage.ProductSortOption.PRICE_LOWER_HIGH;

public class Tests extends BaseTest {
    // https://www.saucedemo.com/

    @Test
    @DisplayName("Complete purchase flow as standard user")
    @Tag("tag_one")
    public void endToEnd() {
        String actualResult = openMainPage()
                .standardUserLogIn()
                .addItemToCart("Sauce Labs Bolt T-Shirt")
                .addItemToCart("Sauce Labs Onesie")
                .clickCartIcon()
                .clickCheckoutButton()
                .fillFirstNameInput("Sergei")
                .fillLastNameInput("Testowy")
                .fillPostalCodeInput("1613")
                .clickContinueButton()
                .clickFinishButton()
                .getCompleteHeaderText();

        String expectedResult = "Thank you for your order!";

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Check visibility and count of cart badge after adding product")
    public void visibleOfShoppingCartBadge() {
        InventoryPage inventoryPage = openMainPage()
                .standardUserLogIn()
                .addRandomAddItemToCart();

        Assertions.assertTrue(inventoryPage.getShoppingCartBadge().isDisplayed());
        Assertions.assertEquals("1", inventoryPage.getShoppingCartBadge().getText());
    }

    @Test
    @DisplayName("Remove product from cart and verify cart is empty")
    public void removeItemFromCart() {
        CartPage cartPage = openMainPage()
                .standardUserLogIn()
                .addRandomAddItemToCart()
                .clickCartIcon();

        Assertions.assertEquals(1, cartPage.getItemsCount());
        Assertions.assertEquals(0, cartPage.clickRemoveButton().getItemsCount());
    }

    @Test
    @DisplayName("Sort products by price from low to high")
    public void sortByPrice() {
        List<Double> list = openMainPage()
                .standardUserLogIn()
                .clickSortDropdown()
                .selectSortOptionByValue(PRICE_LOWER_HIGH)
                .getListOfPrices();

        List<Double> expectedResult = new ArrayList<>(list);
        expectedResult.sort(Comparator.naturalOrder());

        Assertions.assertEquals(expectedResult, list);
    }

    @Test
    @DisplayName("Check title color after mouse hover on product name")
    public void checkTitleColorAfterMouseOver() {
        String actualResult = openMainPage()
                .standardUserLogIn()
                .moveCursorToFirstItemName()
                .getFirstItemColor();

        String expectedResult = "rgba(61, 220, 145, 1)";
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @ParameterizedTest
    @MethodSource("invalidCredentialsData")
    @DisplayName("Log in with invalid credentials and verify error message")
    public void loginWithInvalidCredentials(String login, String password, String expectedErrorMessage) {
        openMainPage().logIn(login, password);
        AuthPage authPage = new AuthPage(driver);
        String actualErrorMessage = authPage.getErrorMessage();

        Assertions.assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    static Stream<Arguments> invalidCredentialsData() {
        return Stream.of(
                Arguments.arguments("locked_out_user", "secret_sauce", "Sorry, this user has been locked out."),
                Arguments.arguments("123", "123", "Username and password do not match any user in this service"),
                Arguments.arguments("123", "", "Password is required"),
                Arguments.arguments("", "123", "Username is required")
        );
    }
}
