package methods;

import config.Config;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseUser {

    protected RequestSpecification getSpec() {
        return given()
                .baseUri(Config.BASE_URL);
    }
}