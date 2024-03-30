import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataTest {
    StepMethods stepMethods = new StepMethods();
    private String newEmail = "";
    private String newPassword = "";

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Change user data with authorization and field name")
    public void changeUserDataWithAuthorizAndName() {
        stepMethods.creationUser();

        Response responseChangeUserData = stepMethods.changeUserDataWithAuthorization(null, null, "Test");
        responseChangeUserData.then().assertThat().body("user.name", equalTo("Test"));
        responseChangeUserData.then().assertThat().body("user.email", equalTo(stepMethods.getEmail()));
    }

    @Test
    @DisplayName("Change user data with authorization and field password")
    public void changeUserDataWithAuthorizAndPassword() {
        stepMethods.creationUser();

        newEmail = stepMethods.getEmail();
        newPassword = "testpass";

        Response responseChangeUserData = stepMethods.changeUserDataWithAuthorization(newEmail, newPassword, null);
        responseChangeUserData.then().assertThat().body("success", equalTo(true));
        responseChangeUserData.then().assertThat().body("user.name", equalTo(stepMethods.getName()));
        responseChangeUserData.then().assertThat().body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Change user data with authorization and field email")
    public void changeUserDataWithAuthorizAndEmail() {
        stepMethods.creationUser();

        newEmail = new Faker().internet().emailAddress();
        newPassword = stepMethods.getPassword();

        Response responseChangeUserData = stepMethods.changeUserDataWithAuthorization(newEmail, newPassword, null);
        responseChangeUserData.then().assertThat().body("user.name", equalTo(stepMethods.getName()));
        responseChangeUserData.then().assertThat().body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Change user data with authorization and all fields")
    public void changeUserDataWithAuthorizAndAllFields() {
        stepMethods.creationUser();

        newEmail = new Faker().internet().emailAddress();
        newPassword = "testpass1";

        Response responseChangeUserData = stepMethods.changeUserDataWithAuthorization(newEmail, newPassword, "TestName");
        responseChangeUserData.then().assertThat().body("success", equalTo(true));
        responseChangeUserData.then().assertThat().body("user.name", equalTo("TestName"));
        responseChangeUserData.then().assertThat().body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Change user data with already used email and with authorization")
    public void changeUserDataWithAlreadyUsedEmail() {
        stepMethods.creationUser();

        newEmail = "test123tset@rambler.ru";
        newPassword = "testpass1";

        stepMethods.creationUser(newEmail, newPassword, "TestName");

        Response responseChangeUserData = stepMethods.changeUserDataWithAuthorization(newEmail, stepMethods.getPassword(), stepMethods.getName());
        responseChangeUserData.then().statusCode(403).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Change user data without authorization and field name")
    public void changeUserDataWithoutAuthorizAndName() {
        stepMethods.creationUser();

        Response responseChangeUserData = stepMethods.changeUserDataWithoutAuthorization(null, null, "Namefortest");
        responseChangeUserData.then().statusCode(401).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Change user data without authorization and field password")
    public void changeUserDataWithoutAuthorizAndPassword() {
        stepMethods.creationUser();

        newEmail = stepMethods.getEmail();
        newPassword = "testpass";

        Response responseChangeUserData = stepMethods.changeUserDataWithoutAuthorization(newEmail, newPassword, null);
        responseChangeUserData.then().statusCode(401).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Change user data without authorization and field email")
    public void changeUserDataWithoutAuthorizAndEmail() {
        stepMethods.creationUser();

        newEmail = new Faker().internet().emailAddress();
        newPassword = stepMethods.getPassword();

        Response responseChangeUserData = stepMethods.changeUserDataWithoutAuthorization(newEmail, newPassword, null);
        responseChangeUserData.then().statusCode(401).assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Change user data without authorization and all fields")
    public void changeUserDataWithoutAuthorizAndAllFields() {
        stepMethods.creationUser();

        newEmail = new Faker().internet().emailAddress();
        newPassword = "testpass1";

        Response responseChangeUserData = stepMethods.changeUserDataWithoutAuthorization(newEmail, newPassword, "TestName");
        responseChangeUserData.then().statusCode(401).assertThat().body("success", equalTo(false));
    }

    @After
    public void afterTest() {
        stepMethods.deleteUser();
        stepMethods.deleteUser(newEmail, newPassword);
    }
}
