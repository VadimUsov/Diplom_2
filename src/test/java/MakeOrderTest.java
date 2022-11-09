import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import methods.OrderClient;
import methods.UserClient;
import order.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class MakeOrderTest {

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
    @DisplayName("Check that authorized user can make order with ingredients")
    public void checkThatAuthorizedUserCanMakeOrderWithIngredients() {
        ValidatableResponse postOrders = sendPostWithAuthAndIngredients();

        checkOrderCreated(postOrders);
    }

    @Test
    @DisplayName("Check that authorized user can't make order without ingredients")
    public void checkThatAuthorizedUserCanNotMakeOrderWithoutIngredients() {
        ValidatableResponse postOrders = sendPostWithAuthWithoutIngredients();

        checkThatOrderNotCreate(postOrders);
    }

    @Test
    @DisplayName("Check that authorized user can't make order with wrong ingredients")
    public void checkThatAuthorizedUserCanNotMakeOrderWithWrongIdIngredients() {
        ValidatableResponse postOrders = sendPostWithAuthWithWrongIngredients();

        checkThatGetInternalServerError(postOrders);
    }

    @Test
    @DisplayName("Check that not authorized user can make order with ingredients")
    public void checkThatNotAuthorizedUserCanMakeOrderWithIngredients() {
        ValidatableResponse postOrders = sendPostWithoutAuthAndWithIngredients();

        checkOrderCreated(postOrders);
    }

    @Test
    @DisplayName("Check that not authorized user can't make order without ingredients")
    public void checkThatNotAuthorizedUserCanNotMakeOrderWithoutIngredients() {
        ValidatableResponse postOrders = sendPostWithoutAuthAndWithoutIngredients();

        checkThatOrderNotCreate(postOrders);
    }

    @Test
    @DisplayName("Check that not authorized user can't make order with wrong ingredients")
    public void checkThatNotAuthorizedUserCanNotMakeOrderWithWrongIdIngredients() {
        ValidatableResponse postOrders = sendPostWithoutAuthWithWrongIngredients();

        checkThatGetInternalServerError(postOrders);
    }

    @Step("Send post request to \"/orders\" with auth and with ingredients")
    public ValidatableResponse sendPostWithAuthAndIngredients() {
        Order order = new Order(Order.getRandomRecipe());
        return orderClient.makeOrderWithAuth(bearerToken, order);
    }

    @Step("Check that order was create")
    public void checkOrderCreated(ValidatableResponse postOrders) {
        postOrders
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Step("Send post request to \"/orders\" with auth without ingredients")
    public ValidatableResponse sendPostWithAuthWithoutIngredients() {
        Order order = new Order();
        return orderClient.makeOrderWithAuth(bearerToken, order);
    }

    @Step("Check that order wasn't create and got correct message")
    public void checkThatOrderNotCreate(ValidatableResponse postOrders) {
        postOrders
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo(orderClient.getMUST_BE_INGREDIENTS()));
    }

    @Step("Send post request to \"/orders\" with auth and with wrong ingredients")
    public ValidatableResponse sendPostWithAuthWithWrongIngredients() {
        Order order = new Order(Order.getWrongRecipe());
        return orderClient.makeOrderWithAuth(bearerToken, order);
    }

    @Step("Check that status code is 500")
    public void checkThatGetInternalServerError(ValidatableResponse postOrders) {
        postOrders
                .statusCode(500);
    }

    @Step("Send post request to \"/orders\" without auth and with ingredients")
    public ValidatableResponse sendPostWithoutAuthAndWithIngredients() {
        Order order = new Order(Order.getRandomRecipe());
        return orderClient.makeOrderWithoutAuth(order);
    }

    @Step("Send post request to \"/orders\" without auth without ingredients")
    public ValidatableResponse sendPostWithoutAuthAndWithoutIngredients() {
        Order order = new Order();
        return orderClient.makeOrderWithoutAuth(order);
    }

    @Step("Send post request to \"/orders\" without auth and with wrong ingredients")
    public ValidatableResponse sendPostWithoutAuthWithWrongIngredients() {
        Order order = new Order(Order.getWrongRecipe());
        return orderClient.makeOrderWithoutAuth(order);
    }
}

