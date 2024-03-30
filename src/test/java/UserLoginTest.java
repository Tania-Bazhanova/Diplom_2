import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {
    StepMethods stepMethods = new StepMethods();

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("User login")
    public void userLogin() {
        stepMethods.creationUser();
        stepMethods.loginUser().then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("User login with incorrecrt email")
    public void userLoginWithIncorrectEmail() {
        stepMethods.creationUser();
        Response loginUser = stepMethods.loginUser("hfvfhkfg", stepMethods.getPassword());
        loginUser.then().statusCode(401).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("User login with incorrecrt password")
    public void userLoginWithIncorrectPassword() {
        stepMethods.creationUser();
        Response loginUser = stepMethods.loginUser(stepMethods.getEmail(), "121561515");
        loginUser.then().statusCode(401).assertThat().body("success", equalTo(false));
    }

    @After
    public void afterTest() {
        stepMethods.deleteUser();
    }
}
