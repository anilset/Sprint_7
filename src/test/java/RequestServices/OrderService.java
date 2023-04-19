package RequestServices;

import io.restassured.response.ValidatableResponse;
import ru.practikum.Order;

import static io.restassured.RestAssured.given;

public class OrderService {

    public final static String ORDER_PATH = "/api/v1/orders";
    public final static String ORDER_LIST_PATH = "/api/v1/orders";
    public ValidatableResponse createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse getListOfOrders() {
        return given()
                .header("Content-type", "application/json")
                .get(ORDER_LIST_PATH)
                .then();
    }
}
