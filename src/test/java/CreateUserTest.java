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
        ValidatableResponse postUser = sendPost(User.getRandomUser());

        saveToken(postUser);

        checkThatCourierWasCreated(postUser);
    }

    @Test
    @DisplayName("Check that can not create two identical user and get error message")
    public void canNotCreateTwoIdenticalUsers() {
        ValidatableResponse postUser = sendPost(User.getRandomUser());

        saveToken(postUser);

        checkThatCourierWasCreated(postUser);

        ValidatableResponse postUserSecondTime = sendPostWithAllDataSecondTime();

        checkThatCreateFailed(postUserSecondTime);
    }

    @Test
    @DisplayName("Check that can not create user without name")
    public void canNotCreateWithoutName() {
        ValidatableResponse postUser = sendPost(User.getUserWithoutName());

        checkThatCreatedFailedWithoutData(postUser);
    }

    @Test
    @DisplayName("Check that can not create user without password")
    public void canNotCreateWithoutPassword() {
        ValidatableResponse postUser = sendPost(User.getUserWithoutPassword());

        checkThatCreatedFailedWithoutData(postUser);
    }

    @Test
    @DisplayName("Check that can not create user without email")
    public void canNotCreateWithoutEmail() {
        ValidatableResponse postUser = sendPost(User.getUserWithoutEmail());

        checkThatCreatedFailedWithoutData(postUser);
    }

    @Step("Send post request to \"/auth/register\"")
    public ValidatableResponse sendPost(User newUser) {
        user = newUser;
        return userClient.createUser(user);
    }

    @Step("Save bearer token for delete user")
    public void saveToken(ValidatableResponse postUser) {
        bearerToken = postUser
                .extract().path("accessToken");
    }

    @Step("Check that user was created")
    public void checkThatCourierWasCreated(ValidatableResponse postUser) {
        postUser
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Step("Send post request to \"/auth/register\" second time")
    public ValidatableResponse sendPostWithAllDataSecondTime() {
        return userClient.createUser(user);
    }

    @Step("Check that create is failed and got correct message")
    public void checkThatCreateFailed(ValidatableResponse postUserSecondTime) {
        postUserSecondTime
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo(userClient.getUSER_EXISTS()));
    }

    @Step("Check that can not create user without data")
    public void checkThatCreatedFailedWithoutData(ValidatableResponse postUser) {
        postUser
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo(userClient.getNOT_ENOUGH_DATE_TO_CREATE()));
    }
}

