package request_services;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import ru.practikum.pojo.Courier;
import ru.practikum.courier_services.CourierCreds;
import ru.practikum.pojo.LoginResponse;

import static io.restassured.RestAssured.given;

public class CourierClient {
    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru/";
    public final static String PATH = "/api/v1/courier";
    public final static String LOGIN_PATH = "/api/v1/courier/login";
    public final static String DELETE_PATH = "/api/v1/courier/";

    public CourierClient() {
        RestAssured.baseURI = BASE_URL;
    }


    public ValidatableResponse createCourier(Courier courier) {
        return  given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(PATH)
                .then();
    }

    public ValidatableResponse login (CourierCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    public ValidatableResponse deleteCourier(LoginResponse id) {
        return given()
                .header("Content-type", "application/json")
                .body(id)
                .delete(DELETE_PATH)
                .then();
    }

    public ValidatableResponse deleteCourier(String id) {
        return given()
                .header("Content-type", "application/json")
                .delete(DELETE_PATH + id)
                .then();
    }

    public String getCourierId(CourierClient courierClient, Courier courier) {
        return courierClient.login(CourierCreds.getCredsFrom(courier)).extract().body().as(LoginResponse.class).getId();
    }

}

