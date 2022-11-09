package methods;

import io.restassured.response.ValidatableResponse;
import order.Order;

public class OrderClient extends BaseUser {

    private final String ORDERS = "/orders";

    private final String MUST_BE_INGREDIENTS = "Ingredient ids must be provided";

    private final String SHOULD_BE_AUTH = "You should be authorised";

    public ValidatableResponse makeOrderWithAuth(String bearerToken, Order order) {
        return getSpec()
                .header("Authorization", bearerToken)
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    public ValidatableResponse makeOrderWithoutAuth(Order order) {
        return getSpec()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    public ValidatableResponse getOrderListWithAuth(String bearerToken) {
        return getSpec()
                .header("Authorization", bearerToken)
                .when()
                .get(ORDERS)
                .then();
    }

    public ValidatableResponse getOrderListWithoutAuth() {
        return getSpec()
                .when()
                .get(ORDERS)
                .then();
    }

    public String getMUST_BE_INGREDIENTS() {
        return MUST_BE_INGREDIENTS;
    }

    public String getSHOULD_BE_AUTH() {
        return SHOULD_BE_AUTH;
    }
}

