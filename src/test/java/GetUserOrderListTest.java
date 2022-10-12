import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import methods.OrderClient;
import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetUserOrderListTest {

    User user;

    UserClient userClient;

    OrderClient orderClient;

    String bearerToken;

    @Before
    public void setup() {
        orderClient = new OrderClient();
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
    @DisplayName("Check that authorized user getting order list")
    public void checkThatAuthorizedUserGettingOrderList() {
        ValidatableResponse getOrders = sendGetOrdersWithAuth();

        checkOrdersWasGetting(getOrders);
    }

    @Step("Send get request to \"/orders\" with auth")
    public ValidatableResponse sendGetOrdersWithAuth() {
        ValidatableResponse getOrders = orderClient.getOrderListWithAuth(bearerToken);
        return getOrders;
    }

    @Step("Check that orders was getting")
    public void checkOrdersWasGetting(ValidatableResponse getOrders) {
        getOrders
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check that not authorized user not getting order list")
    public void checkThatNotAuthorizedUserGettingOrderList() {
        ValidatableResponse getOrders = sendGetOrdersWithoutAuth();

        checkOrdersWasNotGetting(getOrders);
    }

    @Step("Send get request to \"/orders\" without auth")
    public ValidatableResponse sendGetOrdersWithoutAuth() {
        ValidatableResponse getOrders = orderClient.getOrderListWithoutAuth();
        return getOrders;
    }

    @Step("Check that orders wasn't getting and message is correct")
    public void checkOrdersWasNotGetting(ValidatableResponse getOrders) {
        getOrders
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo(orderClient.getSHOULD_BE_AUTH()));
    }
}

