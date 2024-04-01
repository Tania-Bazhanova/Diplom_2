import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest {
    StepMethods stepMethods = new StepMethods();

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("User creation")
    public void userCreate() {
        Response creationUserResponse = stepMethods.creationUser();

        creationUserResponse.then().assertThat().body("success", equalTo(true));

        stepMethods.loginUser().then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("User already created")
    public void  userAlreadyCreated() {
        stepMethods.creationUser();
        Response userAlreadyCreatedResponse = stepMethods.creationUser();

        userAlreadyCreatedResponse.then().statusCode(403).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user without email")
    public void createUserWithoutEmail() {
        //StepMethods stepMethods = new StepMethods();
        Response creationUserResponse = stepMethods.creationUser("", stepMethods.getPassword(), stepMethods.getName());

        creationUserResponse.then().statusCode(403).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user without password")
    public void createUserWithoutPassword() {
        //StepMethods stepMethods = new StepMethods();
        Response creationUserResponse = stepMethods.creationUser(stepMethods.getEmail(), "", stepMethods.getName());

        creationUserResponse.then().statusCode(403).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Create user without name")
    public void createUserWithoutName() {
        //StepMethods stepMethods = new StepMethods();
        Response creationUserResponse = stepMethods.creationUser(stepMethods.getEmail(), stepMethods.getPassword(), "");

        creationUserResponse.then().statusCode(403).assertThat().body("success", equalTo(false));
    }

    @After
    public void afterTest() {
        stepMethods.deleteUser();
    }
}
