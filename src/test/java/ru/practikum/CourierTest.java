package ru.practikum;

import jdk.jfr.Description;
import request_services.CourierClient;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.practikum.courier_services.CourierCreds;
import ru.practikum.pojo.Courier;
import ru.practikum.pojo.LoginResponse;
import ru.practikum.pojo.Ok;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static ru.practikum.courier_services.CourierGenerator.getRandomCourier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourierTest {

    private CourierClient courierClient;
    List<String> testCourierIds = new ArrayList<>();

    public void checkIfCourierCreated(Courier courier) {
        String id =courierClient.getCourierId(courierClient, courier);
        if(id != null) {
            testCourierIds.add(id);
        }
    }

    @BeforeEach
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Проверка запроса на создание курьера")
    public void createCourierCheck() {
        Courier courier = getRandomCourier();
        ValidatableResponse response = courierClient.createCourier(courier);
        Ok ok = response.extract().body().as(Ok.class);
        checkIfCourierCreated(courier);
        assertAll (
                ()-> assertEquals(SC_CREATED, response.extract().statusCode()),
                ()-> assertTrue(ok.getOk())
        );
    }

    @Test
    @DisplayName("Проверка невозможности создания двух курьеров с одинаковым логином")
    public void createCourierWithSimilarLoginCheck() {
        Integer statusCode;
        String login = new Utilities().nextLogin;
        for (int i = 0; i < 2; i++) {
            Courier courier = new Courier(login, new Utilities().nextPw, new Utilities().nextFirstName);
            courierClient.createCourier(courier);
            checkIfCourierCreated(courier);
            if (i == 1) {
                statusCode = courierClient.createCourier(courier).extract().statusCode();
                assertEquals(409, statusCode, "Этот логин уже используется");
            }
        }
    }

    private static Stream<Arguments> provideBlankCredentials() {
        return Stream.of(
                of("", new Utilities().nextPw, new Utilities().nextFirstName, 400),
                of(new Utilities().nextLogin, "", new Utilities().nextFirstName, 400),
                of("", "", new Utilities().nextFirstName, 400)
        );
    }

    @ParameterizedTest (name = "Проверка запроса на создание курьера с пустыми полями Логин Пароль")
    @MethodSource("provideBlankCredentials")
    public void createCourierWithNoLoginOrPassword(String login, String pw, String name, Integer expected) {
        Courier courier = new Courier(login, pw, name);
        ValidatableResponse response = courierClient.createCourier(courier);
        checkIfCourierCreated(courier);
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

    @ParameterizedTest (name = "Проверка запроса на логин курьера")
    @MethodSource("provideCredentialsToLogin")
    public void courierLoginCheck(String login, String pw, Integer expected) {
        Courier courier = new Courier(login, pw, new Utilities().nextFirstName);
        courierClient.createCourier(courier);
        CourierCreds creds = new CourierCreds(login, pw);
        ValidatableResponse response1 = courierClient.login(creds);
        checkIfCourierCreated(courier);
        assertEquals(expected, response1.extract().statusCode());
    }

    @Test
    @DisplayName("Проверка наличия id в теле ответа на запрос логина курьером")
    public void courierIdAfterLoginCheck() {
        Courier courier = getRandomCourier();
        courierClient.createCourier(courier);
        checkIfCourierCreated(courier);
        LoginResponse response = courierClient.login(CourierCreds.getCredsFrom(courier)).extract().body().as(LoginResponse.class);
        assertNotNull(response.getId());
    }

    @Test
    @DisplayName("Проверка удаления курьера")
    @Description("Ошибка в документации: запрос не должен содержать id в теле")
    public void deleteCourierCheck() {
        Courier courier = getRandomCourier();
        courierClient.createCourier(courier);
        checkIfCourierCreated(courier);
        String idToDelete = courierClient.getCourierId(courierClient, courier);
        ValidatableResponse deleteResponse = courierClient.deleteCourier(idToDelete);
        Ok ok = deleteResponse.extract().body().as(Ok.class);
            assertAll (
                    ()-> assertEquals(HttpStatus.SC_OK, deleteResponse.extract().statusCode()),
                    ()-> assertTrue(ok.getOk())
                );
    }

    @AfterAll
    public void clearCourierList() {
        assertTrue(!testCourierIds.isEmpty());
        for (String idToDelete: testCourierIds) {
            courierClient.deleteCourier(idToDelete);
        }
    }
}



