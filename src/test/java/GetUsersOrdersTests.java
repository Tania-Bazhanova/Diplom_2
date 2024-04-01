import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class GetUsersOrdersTests {

    StepMethods stepMethods = new StepMethods();
    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Get users orders with authorisation")
    public void getUsersOrdersWithAuthorization() {
        List<String> ingredients = stepMethods.getFieldEngredients();
        List<String> useIngredients = new ArrayList<>();
        useIngredients.add(ingredients.get(0));
        useIngredients.add(ingredients.get(12));

        stepMethods.creationUser();
        stepMethods.createOrderWithAuthorization(useIngredients);

        Response responseUsersOrders = stepMethods.getUserOrderWithAuthorization();
        responseUsersOrders.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Get users orders without authorisation")
    public void getUsersOrdersWithoutAuthorization() {
        List<String> ingredients = stepMethods.getFieldEngredients();
        List<String> useIngredients = new ArrayList<>();
        useIngredients.add(ingredients.get(0));
        useIngredients.add(ingredients.get(12));

        stepMethods.creationUser();
        stepMethods.createOrderWithAuthorization(useIngredients);

        Response responseUsersOrders = stepMethods.getUserOrderWithoutAuthorization();
        responseUsersOrders.then().statusCode(401).assertThat().body("success", equalTo(false));
    }

    @After
    public void afterTest() {
        stepMethods.deleteUser();
    }
}
