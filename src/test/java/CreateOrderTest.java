import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
    StepMethods stepMethods = new StepMethods();

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Create order with authorization and ingredients")
    public void createOrderWithAuthorizAndIngredients() {
        List<String> ingredients = stepMethods.getFieldEngredients();
        List<String> useIngredients = new ArrayList<>();
        useIngredients.add(ingredients.get(0));
        useIngredients.add(ingredients.get(3));
        useIngredients.add(ingredients.get(6));

        stepMethods.creationUser();
        Response responseCreateOrder = stepMethods.createOrderWithAuthorization(useIngredients);
        responseCreateOrder.then().assertThat().body("order.number", notNullValue()).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order without authorization and with ingredients")
    public void createOrderWithoutAuthorizAndWithIngredients() {
        List<String> ingredients = stepMethods.getFieldEngredients();
        List<String> useIngredients = new ArrayList<>();
        useIngredients.add(ingredients.get(8));
        useIngredients.add(ingredients.get(5));
        useIngredients.add(ingredients.get(4));

        Response responseCreateOrder = stepMethods.createOrderWithoutAuthorization(useIngredients);

        responseCreateOrder.then().assertThat().body("order.number", notNullValue()).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order with authorization and without ingredients")
    public void createOrderWithAuthorizAndWithoutIngredients() {
        stepMethods.creationUser();
        Response responseCreateOrder = stepMethods.createOrderWithAuthorization(null);
        responseCreateOrder.then().statusCode(400).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create order without authorization and ingredients")
    public void createOrderWithoutAuthorizAndIngredients() {
        Response responseCreateOrder = stepMethods.createOrderWithoutAuthorization(null);
        responseCreateOrder.then().statusCode(400).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create order with authorization and with incorrect ingredients")
    public void createOrderWithAuthorizAndWithIncorrectIngredients() {
        List<String> ingredients = stepMethods.getFieldEngredients();
        List<String> useIngredients = new ArrayList<>();
        useIngredients.add(ingredients.get(0));
        useIngredients.add("565532323");

        stepMethods.creationUser();
        Response responseCreateOrder = stepMethods.createOrderWithAuthorization(useIngredients);
        responseCreateOrder.then().statusCode(500);
    }

    @Test
    @DisplayName("Create order without authorization and with incorrect ingredients")
    public void createOrderWithoutAuthorizAndtIngredients() {
        List<String> ingredients = stepMethods.getFieldEngredients();
        List<String> useIngredients = new ArrayList<>();
        useIngredients.add("jhgnfgmkfgm");
        useIngredients.add(ingredients.get(3));

        Response responseCreateOrder = stepMethods.createOrderWithoutAuthorization(useIngredients);
        responseCreateOrder.then().statusCode(500);
    }

    @After
    public void afterTest() {
        stepMethods.deleteUser();
    }
}
