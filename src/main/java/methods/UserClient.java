package methods;

import io.restassured.response.ValidatableResponse;
import user.User;
import user.UserForLogin;

public class UserClient extends BaseUser {

    private final String CREATE_USER = "/auth/register";

    private final String LOGIN_USER = "/auth/login";

    private final String CHANGE_USER = "/auth/user";

    private final String USER_EXISTS = "User already exists";

    private final String NOT_ENOUGH_DATE_TO_CREATE = "Email, password and name are required fields";

    private final String LOGIN_FAILED = "email or password are incorrect";

    private final String SHOULD_BE_AUTH = "You should be authorised";

    public ValidatableResponse createUser(User user) {
        return getSpec()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(CREATE_USER)
                .then()
                .assertThat();
    }

    public ValidatableResponse loginUser(UserForLogin userForLogin) {
        return getSpec()
                .header("Content-Type", "application/json")
                .body(userForLogin)
                .when()
                .post(LOGIN_USER)
                .then()
                .assertThat();
    }

    public void deleteUser(String bearerToken) {
        getSpec()
                .header("Authorization", bearerToken)
                .when()
                .delete(CHANGE_USER)
                .then()
                .statusCode(202);

    }

    public ValidatableResponse changeUserWithAuth(String bearerToken, User changeUser) {
        return getSpec()
                .header("Authorization", bearerToken)
                .header("Content-Type", "application/json")
                .body(changeUser)
                .when()
                .patch(CHANGE_USER)
                .then();
    }

    public ValidatableResponse changeUserWithoutAuth(User changeUser) {
        return getSpec()
                .header("Content-Type", "application/json")
                .body(changeUser)
                .when()
                .patch(CHANGE_USER)
                .then()
                .assertThat();
    }

    public String getUSER_EXISTS() {
        return USER_EXISTS;
    }

    public String getNOT_ENOUGH_DATE_TO_CREATE() {
        return NOT_ENOUGH_DATE_TO_CREATE;
    }

    public String getLOGIN_FAILED() {
        return LOGIN_FAILED;
    }

    public String getSHOULD_BE_AUTH() {
        return SHOULD_BE_AUTH;
    }
}

