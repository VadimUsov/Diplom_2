import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {

    User user;

    UserClient userClient;

    String bearerToken = "";

    @Before
    public void setup() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (bearerToken.length() > 0)
            userClient.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Check that user can be created with all data")
    public void createUserWithAllData() {
        ValidatableResponse postUser = sendPostWithAllData();

        checkThatCourierWasCreated(postUser);
    }

    @Step("Send post request to \"/auth/register\" with all data")
    public ValidatableResponse sendPostWithAllData() {
        user = User.getRandomUser();
        ValidatableResponse postUser = userClient.createUser(user);
        bearerToken = postUser
                .extract().path("accessToken");
        return postUser;
    }

    @Step("Check that user was created")
    public void checkThatCourierWasCreated(ValidatableResponse postUser) {
        postUser
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check that can not create two identical user and get error message")
    public void canNotCreateTwoIdenticalUsers() {
        ValidatableResponse postUser = sendPostWithAllData();

        checkThatCourierWasCreated(postUser);

        ValidatableResponse postUserSecondTime = sendPostWithAllDataSecondTime();

        checkThatCreateFailed(postUserSecondTime);
    }

    @Step("Send post request to \"/auth/register\" second time")
    public ValidatableResponse sendPostWithAllDataSecondTime() {
        ValidatableResponse postUserSecondTime = userClient.createUser(user);
        return postUserSecondTime;
    }

    @Step("Check that create is failed and got correct message")
    public void checkThatCreateFailed(ValidatableResponse postUserSecondTime) {
        postUserSecondTime
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo(userClient.getUSER_EXISTS()));
    }

    @Test
    @DisplayName("Check that can not create user without name")
    public void canNotCreateWithoutName() {
        ValidatableResponse postUser = sendPostWithoutName();

        checkThatCreatedFailedWithoutData(postUser);
    }

    @Step("Send post request to \"/auth/register\" without name")
    public ValidatableResponse sendPostWithoutName() {
        user = User.getUserWithoutName();
        ValidatableResponse postUser = userClient.createUser(user);
        return postUser;
    }

    @Step("Check that can not create user without data")
    public void checkThatCreatedFailedWithoutData(ValidatableResponse postUser) {
        postUser
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo(userClient.getNOT_ENOUGH_DATE_TO_CREATE()));
    }

    @Test
    @DisplayName("Check that can not create user without password")
    public void canNotCreateWithoutPassword() {
        ValidatableResponse postUser = sendPostWithoutPassword();

        checkThatCreatedFailedWithoutData(postUser);
    }

    @Step("Send post request to \"/auth/register\" without password")
    public ValidatableResponse sendPostWithoutPassword() {
        user = User.getUserWithoutPassword();
        ValidatableResponse postUser = userClient.createUser(user);
        return postUser;
    }

    @Test
    @DisplayName("Check that can not create user without email")
    public void canNotCreateWithoutEmail() {
        ValidatableResponse postUser = sendPostWithoutEmail();

        checkThatCreatedFailedWithoutData(postUser);
    }

    @Step("Send post request to \"/auth/register\" without email")
    public ValidatableResponse sendPostWithoutEmail() {
        user = User.getUserWithoutEmail();
        ValidatableResponse postUser = userClient.createUser(user);
        return postUser;
    }

}

