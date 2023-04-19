package ru.practikum;

import RequestServices.OrderService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static ru.practikum.Config.BASE_URL;
import static ru.practikum.Utilities.TOMORROW;

public class OrderTest {
    OrderService service = new OrderService();
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
    @ParameterizedTest
    @MethodSource("provideColours")
    public void checkOrder(List <String> colours) {
        Order order = new OrderGenerator().getRandomOrder(colours);
        ValidatableResponse response = service.createOrder(order);
        assertAll(
                ()-> assertEquals(201, response.extract().statusCode()),
                ()-> assertThat(response.extract().path("track"), notNullValue())
        );
    }

    @Test
    public void checkListOfOrders() {
        ValidatableResponse response = service.getListOfOrders();
        List <Response> responses = response.extract().path("orders");
        assertAll(
                ()->assertEquals(200, response.extract().statusCode()),
                ()-> assertTrue(responses.size() > 0)
        );
    }
}