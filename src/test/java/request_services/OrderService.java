package request_services;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.practikum.pojo.Order;
import ru.practikum.pojo.Track;

import static io.restassured.RestAssured.given;

public class OrderService {

    public final static String ORDER_PATH = "/api/v1/orders";
    public final static String ORDER_LIST_PATH = "/api/v1/orders";
    public final static String CANCEL_ORDER_PATH = "/api/v1/orders/cancel";
    public final static String ORDER_BY_TRACK_PATH = "/v1/orders/track?t=";

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

    public ValidatableResponse cancelOrder(Track track) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(track)
                .when()
                .put(CANCEL_ORDER_PATH)
                .then();
    }

    public ValidatableResponse getOrderByTrack(String number) {
        return given()
                .header("Content-type", "application/json")
                .get(ORDER_BY_TRACK_PATH + number)
                .then();
    }
}
