package RequestServices;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.practikum.Courier;
import ru.practikum.CourierCreds;
import ru.practikum.DeleteRequest;
import ru.practikum.LoginResponse;

import static io.restassured.RestAssured.given;

public class CourierClient {
    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru/";
    public final static String PATH = "/api/v1/courier";
    public final static String LOGIN_PATH = "/api/v1/courier/login";
    public final static String DELETE_PATH = "/api/v1/courier/:id";

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

    public ValidatableResponse deleteCourier(DeleteRequest id) {
        return given()
                .header("Content-type", "application/json")
                .body(id)
                .delete(DELETE_PATH)
                .then();
    }

}

