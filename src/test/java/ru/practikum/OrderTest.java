package ru.practikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import request_services.OrderService;
import ru.practikum.order_services.OrderGenerator;
import ru.practikum.pojo.Order;
import ru.practikum.pojo.Track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static ru.practikum.Config.BASE_URL;

public class OrderTest {
    OrderService service = new OrderService();
    List <Track> trackNumbers = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    private static Stream<Arguments> provideColours() {
        List<String> colours = new ArrayList<>();
        return Stream.of(
                of(Arrays.asList("GREY")),
                of(Arrays.asList("BLACK")),
                of(Arrays.asList("")),
                of(Arrays.asList("BLACK", "GREY"))
        );
    }

    @ParameterizedTest (name = "Проверка создания заказа. Проверка необязательного поля Цвет")
    @MethodSource("provideColours")
    public void checkOrder(List<String> colours) {
        Order order = new OrderGenerator().getRandomOrder(colours);
        ValidatableResponse response = service.createOrder(order);
        Track track = response.extract().body().as(Track.class);
        trackNumbers.add(track);
        assertAll(
                () -> assertEquals(HttpStatus.SC_CREATED, response.extract().statusCode()),
                () -> assertNotNull(track.getTrack())
        );
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void checkListOfOrders() {
        ValidatableResponse response = service.getListOfOrders();
        List<Response> responses = response.extract().path("orders");
        assertAll(
                () -> assertEquals(HttpStatus.SC_OK, response.extract().statusCode()),
                () -> assertTrue(responses.size() > 0)
        );
    }

   @AfterEach
    public void cancelTestOrders() {
        if (!trackNumbers.isEmpty()) {
            for (Track track : trackNumbers) {
                String number = track.getTrack();
                ValidatableResponse cancel = service.cancelOrder(new Track(number));
            }
        }
    }
}