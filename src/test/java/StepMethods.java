import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import datafortest.DataForOrderCreate;
import datafortest.DataForUserCreate;
import datafortest.DataForUserLogin;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.given;

@Getter
public class StepMethods {

    Faker faker = new Faker(Locale.ENGLISH);

    private final String email = faker.internet().emailAddress();
    private final String password = faker.internet().password(5, 10);
    private final String name = faker.name().firstName();


    @Step("Send POST request to /api/auth/register")
    public Response creationUser() {
        return creationUser(email, password, name);
    }

    @Step("Send POST request to /api/auth/register")
    public Response creationUser(String email, String password, String name) {

        Response responseCreation =
                given()
                        .header("Content-type", "application/json")
                        .body(new DataForUserCreate(email, password, name))
                        .post("api/auth/register");

        return responseCreation;
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUser() {
        return loginUser(email, password);
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUser(String email, String password) {
        Response responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .body(new DataForUserLogin(email, password))
                        .post("api/auth/login");

        return responseLogin;
    }

    @Step("Get field authorization")
    public String getFieldAuthorization() {
        return getFieldAuthorization(email, password);
    }

    @Step("Get field authorization")
    public String getFieldAuthorization(String email, String password) {
        Gson gson = new Gson();
        JsonObject objectLoginResponse = gson.fromJson(loginUser(email, password).body().asString(), JsonObject.class);

        if (objectLoginResponse.get("accessToken") != null) {
            return objectLoginResponse.get("accessToken").getAsString();
        }
        return "";
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response deleteUser() {
        return deleteUser(email, password);
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response deleteUser(String email, String password) {
        Response responseDelete =
                given()
                        .header("Content-type", "application/json")
                        .header("authorization", getFieldAuthorization(email, password))
                        .delete("api/auth/user");

        return responseDelete;
    }

    @Step("Send PATCH request with authorization to /api/auth/user")
    public Response changeUserDataWithAuthorization(String email, String password, String name) {
        Response responseChangeData =
                given()
                        .header("Content-type", "application/json")
                        .header("authorization", getFieldAuthorization())
                        .body(new DataForUserCreate(email, password, name))
                        .patch("api/auth/user");
        return responseChangeData;
    }

    @Step("Send PATCH request without authorization to /api/auth/user")
    public Response changeUserDataWithoutAuthorization(String email, String password, String name) {
        Response responseChangeData =
                given()
                        .header("Content-type", "application/json")
                        .body(new DataForUserCreate(email, password, name))
                        .patch("api/auth/user");
        return responseChangeData;
    }


    @Step("Send GET request to api/ingredients")
    public Response getIngredients() {
        Response responseIngredients =
                given()
                        .get("api/ingredients");
        return responseIngredients;
    }

    @Step("Get field ingredients")
    public List<String> getFieldEngredients() {
        Gson gson = new Gson();
        JsonArray listIngredients = gson.fromJson(getIngredients().body().asString(), JsonObject.class).getAsJsonArray("data");
        List<String> ingredients = new ArrayList<>();
        for (JsonElement ingredient : listIngredients) {
            ingredients.add(ingredient.getAsJsonObject().get("_id").getAsString());
        }
        return ingredients;
    }

    @Step("Send POST request to api/orders with authorization")
    public Response createOrderWithAuthorization(List<String> ingredients) {
        Response responseOrderCreation =
                given()
                        .header("Content-type", "application/json")
                        .header("authorization", getFieldAuthorization())
                        .body(new DataForOrderCreate(ingredients))
                        .post("api/orders");
        return responseOrderCreation;
    }

    @Step("Send POST request to api/orders without authorization")
    public Response createOrderWithoutAuthorization(List<String> ingredients) {
        Response responseOrderCreation =
                given()
                        .header("Content-type", "application/json")
                        .body(new DataForOrderCreate(ingredients))
                        .post("api/orders");
        return responseOrderCreation;
    }

    @Step("Send GET request to api/orders with authorization")
    public Response getUserOrderWithAuthorization() {
        Response responseUserOrder =
                given()
                        .header("authorization", getFieldAuthorization())
                        .get("api/orders");

        return responseUserOrder;
    }

    @Step("Send GET request to api/orders without authorization")
    public Response getUserOrderWithoutAuthorization() {
        Response responseUserOrder =
                given()
                        .get("api/orders");

        return responseUserOrder;
    }
}
