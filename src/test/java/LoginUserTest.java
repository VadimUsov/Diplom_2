import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserForLogin;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {

    User user;

    UserForLogin userForLogin;

    UserClient userClient;

    String bearerToken;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        bearerToken = userClient.createUser(user)
                .statusCode(200)
                .extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("User can login with all data")
    public void loginUserWithAllData() {
        ValidatableResponse postUser = postOrdersWithAllData();

        checkThatCanLogin(postUser);
    }

    @Step("Send post request to \"/auth/login\" with all data")
    public ValidatableResponse postOrdersWithAllData() {
        userForLogin = UserForLogin.from(user);
        ValidatableResponse postUser = userClient.loginUser(userForLogin);
        return postUser;
    }

    @Step("Check that user can be login with all data")
    public void checkThatCanLogin(ValidatableResponse postUser) {
        postUser
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("User can't login without email")
    public void loginWithWrongEmail() {
        ValidatableResponse postUser = postOrdersWithoutEmail();

        checkThatCanNotLoginWithoutAllData(postUser);
    }

    @Step("Send post request to \"/auth/login\" without email")
    public ValidatableResponse postOrdersWithoutEmail() {
        userForLogin = UserForLogin.withWrongEmail(user);
        ValidatableResponse postUser = userClient.loginUser(userForLogin);
        return postUser;
    }

    @Step("Check that user can't be login without all data")
    public void checkThatCanNotLoginWithoutAllData(ValidatableResponse postUser) {
        postUser
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo(userClient.getLOGIN_FAILED()));

    }

    @Test
    @DisplayName("User can't login without password")
    public void loginWithWrongPassword() {
        ValidatableResponse postUser = postOrdersWithoutPassword();

        checkThatCanNotLoginWithoutAllData(postUser);
    }

    @Step("Send post request to \"/auth/login\" without password")
    public ValidatableResponse postOrdersWithoutPassword() {
        userForLogin = UserForLogin.withWrongPassword(user);
        ValidatableResponse postUser = userClient.loginUser(userForLogin);
        return postUser;
    }
}

