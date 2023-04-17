package ru.practikum;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static ru.practikum.CourierGenerator.getRandomCourier;

public class CourierTest {

    private CourierClient courierClient;

    @BeforeEach
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    public void createCourierCheck() {
        Courier courier = getRandomCourier();
        ValidatableResponse response = courierClient.create(courier);
        Ok ok = response.extract().body().as(Ok.class);
        assertAll (
                ()-> assertEquals(SC_CREATED, response.extract().statusCode()),
                ()-> assertTrue(ok.getOk())
        );
    }

    @Test
    public void createCourierWithSimilarLoginCheck() {
        String login = new Utilities().nextLogin;
        Courier courier1 = new Courier(login, new Utilities().nextPw, new Utilities().nextFirstName);
        Courier sameCourier = new Courier(login, new Utilities().nextPw, new Utilities().nextFirstName);
        courierClient.create(courier1);
        ValidatableResponse response2 = courierClient.create(sameCourier);
        assertEquals(HttpStatus.SC_CONFLICT, response2.extract().statusCode(), "Этот логин уже используется");
    }

    private static Stream<Arguments> provideBlankCredentials() {
        return Stream.of(
                of("", new Utilities().nextPw, new Utilities().nextFirstName, 400),
                of(new Utilities().nextLogin, "", new Utilities().nextFirstName, 400),
                of("", "", new Utilities().nextFirstName, 400)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBlankCredentials")
    public void createCourierWithNoLoginOrPassword(String login, String pw, String name, Integer expected) {
        Courier courier = new Courier(login, pw, name);
        ValidatableResponse response = courierClient.create(courier);
        assertEquals(expected, response.extract().statusCode(), "Недостаточно данных для создания учетной записи");

    }

    private static Stream<Arguments> provideCredentialsToLogin() {
        String login = new Utilities().nextLogin;
        String pw = new Utilities().nextPw;
        return Stream.of(
                of(login, pw, 200),
                of(login, "", 400),
                of("", pw, 400),
                of("", "", 400),
                of(new Utilities().nextLogin, pw, 404),
                of(login, new Utilities().nextPw, 404),
                of(new Utilities().nextLogin, new Utilities().nextPw, 404)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCredentialsToLogin")
    public void courierLoginCheck(String login, String pw, Integer expected) {
        Courier courier = new Courier(login, pw, new Utilities().nextFirstName);
        courierClient.create(courier);
        CourierCreds creds = new CourierCreds(login, pw);
        ValidatableResponse response1 = courierClient.login(creds);
        assertEquals(expected, response1.extract().statusCode());
    }

    @Test
    public void courierLoginCheck() {
        Courier courier = getRandomCourier();
        courierClient.create(courier);
        LoginResponse response = courierClient.login(CourierCreds.getCredsFrom(courier)).extract().body().as(LoginResponse.class);
        assertThat(response.getId(), notNullValue());
    }

    @Test
    public void deleteCourierCheck() {
        Courier courier = getRandomCourier();
        courierClient.create(courier);
        courierClient.login(CourierCreds.getCredsFrom(courier));
        LoginResponse login = courierClient.login(CourierCreds.getCredsFrom(courier)).extract().body().as(LoginResponse.class);
        Response response = courierClient.deleteCourier(login);
        assertEquals(200, response.statusCode());
    }
}



