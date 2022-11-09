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
        ValidatableResponse patchUser = sendPatchChangeDataRequestWithAuth(new User(user.getEmail(), user.getPassword(), (user.getName() + changePart)));

        checkThatDataWasChanged(patchUser);
    }

    @Test
    @DisplayName("Check that authorized user can change email")
    public void checkThatAuthorizedUserCanChangeEmail() {
        ValidatableResponse patchUser = sendPatchChangeDataRequestWithAuth(new User((changePart + user.getEmail()), user.getPassword(), user.getName()));

        checkThatDataWasChanged(patchUser);
    }

    @Test
    @DisplayName("Check that authorized user can change password")
    public void checkThatAuthorizedUserCanChangePassword() {
        ValidatableResponse patchUser = sendPatchChangeDataRequestWithAuth(new User(user.getEmail(), user.getPassword() + changePart, user.getName()));
        checkThatDataWasChanged(patchUser);
    }

    @Test
    @DisplayName("Check that not authorized user can't change name")
    public void checkThatNotAuthorizedUserCanNotChangeName() {
        ValidatableResponse patchUser = sendPatchChangeDataRequestWithoutAuth(new User(user.getEmail(), user.getPassword(), user.getName() + changePart));

        checkThatDataWasNotChanged(patchUser);
    }

    @Test
    @DisplayName("Check that not authorized user can't change email")
    public void checkThatNotAuthorizedUserCanNotChangeEmail() {
        ValidatableResponse patchUser = sendPatchChangeDataRequestWithoutAuth(new User(changePart + user.getEmail(), user.getPassword(), user.getName()));

        checkThatDataWasNotChanged(patchUser);
    }

    @Test
    @DisplayName("Check that not authorized user can't change password")
    public void checkThatNotAuthorizedUserCanNotChangePassword() {
        ValidatableResponse patchUser = sendPatchChangeDataRequestWithoutAuth(new User(user.getEmail(), user.getPassword() + changePart, user.getName()));
        checkThatDataWasNotChanged(patchUser);
    }

    @Step("Send patch request to \"/auth/user\" with auth and changed data")
    public ValidatableResponse sendPatchChangeDataRequestWithAuth(User changeUser) {
        return userClient.changeUserWithAuth(bearerToken, changeUser);
    }

    @Step("Check that user's data was changed")
    public void checkThatDataWasChanged(ValidatableResponse patchUser) {
        patchUser
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Step("Send patch request to \"/auth/user\" without auth and with changed data")
    public ValidatableResponse sendPatchChangeDataRequestWithoutAuth(User changeUser) {
        return userClient.changeUserWithoutAuth(changeUser);
    }

    @Step("Check that user's data wasn't changed and got correct message")
    public void checkThatDataWasNotChanged(ValidatableResponse patchUser) {
        patchUser
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo(userClient.getSHOULD_BE_AUTH()));
    }
}
