package methods;

import java.util.ArrayList;

public class Ingredients extends BaseUser {

    private final String INGREDIENTS = "/ingredients";

    public ArrayList<String> getIngredients() {
        return getSpec()
                .when()
                .get(INGREDIENTS)
                .then()
                .statusCode(200)
                .extract().path("data._id");
    }
}

