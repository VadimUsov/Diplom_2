import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserTest {

    String changePart = "we";
    User user;

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
    @DisplayName("Check that authorized user can change name")
    public void checkThatAuthorizedUserCanChangeName() {
        ValidatableResponse patchUser = sendPatchChangeNameRequestWithAuth();

        checkThatDataWasChanged(patchUser);
    }

    @Step("Send patch request to \"/auth/user\" with auth and changed name")
    public ValidatableResponse sendPatchChangeNameRequestWithAuth() {
        User changeUser = new User(user.getEmail(), user.getPassword(), (user.getName() + changePart));
        ValidatableResponse patchUser = userClient.changeUserWithAuth(bearerToken, changeUser);
        return patchUser;
    }

    @Step("Check that user's data was changed")
    public void checkThatDataWasChanged(ValidatableResponse patchUser) {
        patchUser
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check that authorized user can change email")
    public void checkThatAuthorizedUserCanChangeEmail() {
        ValidatableResponse patchUser = sendPatchChangeEmailRequestWithAuth();

        checkThatDataWasChanged(patchUser);
    }

    @Step("Send patch request to \"/auth/user\" with auth and changed email")
    public ValidatableResponse sendPatchChangeEmailRequestWithAuth() {
        User changeUser = new User((changePart + user.getEmail()), user.getPassword(), user.getName());
        ValidatableResponse patchUser = userClient.changeUserWithAuth(bearerToken, changeUser);
        return patchUser;
    }

    @Test
    @DisplayName("Check that authorized user can change password")
    public void checkThatAuthorizedUserCanChangePassword() {
        ValidatableResponse patchUser = sendPatchChangePasswordRequestWithAuth();

        checkThatDataWasChanged(patchUser);
    }

    @Step("Send patch request to \"/auth/user\" with auth and changed password")
    public ValidatableResponse sendPatchChangePasswordRequestWithAuth() {
        User changeUser = new User(user.getEmail(), user.getPassword() + changePart, user.getName());
        ValidatableResponse patchUser = userClient.changeUserWithAuth(bearerToken, changeUser);
        return patchUser;
    }

    @Test
    @DisplayName("Check that not authorized user can't change name")
    public void checkThatNotAuthorizedUserCanNotChangeName() {
        ValidatableResponse patchUser = sendPatchChangeNamedRequestWithoutAuth();

        checkThatDataWasNotChanged(patchUser);
    }

    @Step("Send patch request to \"/auth/user\" without auth and with changed name")
    public ValidatableResponse sendPatchChangeNamedRequestWithoutAuth() {
        User changeUser = new User(user.getEmail(), user.getPassword(), user.getName() + changePart);
        ValidatableResponse patchUser = userClient.changeUserWithoutAuth(changeUser);
        return patchUser;
    }

    @Step("Check that user's data wasn't changed and got correct message")
    public void checkThatDataWasNotChanged(ValidatableResponse patchUser) {
        patchUser
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo(userClient.getSHOULD_BE_AUTH()));
    }

    @Test
    @DisplayName("Check that not authorized user can't change email")
    public void checkThatNotAuthorizedUserCanNotChangeEmail() {
        ValidatableResponse patchUser = sendPatchChangeEmailRequestWithoutAuth();

        checkThatDataWasNotChanged(patchUser);
    }

    @Step("Send patch request to \"/auth/user\" without auth and with changed email")
    public ValidatableResponse sendPatchChangeEmailRequestWithoutAuth() {
        User changeUser = new User(changePart + user.getEmail(), user.getPassword(), user.getName());
        ValidatableResponse patchUser = userClient.changeUserWithoutAuth(changeUser);
        return patchUser;
    }

    @Test
    @DisplayName("Check that not authorized user can't change password")
    public void checkThatNotAuthorizedUserCanNotChangePassword() {
        ValidatableResponse patchUser = sendPatchChangePasswordRequestWithoutAuth();

        checkThatDataWasNotChanged(patchUser);
    }

    @Step("Send patch request to \"/auth/user\" without auth and with changed password")
    public ValidatableResponse sendPatchChangePasswordRequestWithoutAuth() {
        User changeUser = new User(user.getEmail(), user.getPassword() + changePart, user.getName());
        ValidatableResponse patchUser = userClient.changeUserWithoutAuth(changeUser);
        return patchUser;
    }
}
