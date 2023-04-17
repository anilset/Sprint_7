package ru.practikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient {
    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru/";
    public final static String PATH = "/api/v1/courier";
    public final static String LOGIN_PATH = "/api/v1/courier/login";
    public final static String DELETE_PATH = "/api/v1/courier/:id";

    public CourierClient() {
        RestAssured.baseURI = BASE_URL;
    }


    public ValidatableResponse create(Courier courier) {
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

    public Response deleteCourier(LoginResponse id) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(id)
                .when()
                .delete(DELETE_PATH);
    }

}

