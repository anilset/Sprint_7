package ru.practikum.order_services;

import ru.practikum.Utilities;
import ru.practikum.pojo.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static ru.practikum.Utilities.TOMORROW;

public class OrderGenerator {

    Random random = new Random();
    public Order getRandomOrder(List<String> colours) {
        return new Order()
                .setFirstName(new Utilities().nextFirstName)
                .setLastName(new Utilities().nextFirstName)
                .setAddress(new Utilities().nextAddress)
                .setMetroStation(random.nextInt(10))
                .setPhone(new Utilities().nextPhoneNumber)
                .setRentTime(random.nextInt(10))
                .setDeliveryDate(TOMORROW)
                .setComment(new Utilities().nextAddress)
                .setColor(colours);
    }

    public static void main(String[] args) {
        Order order = new OrderGenerator().getRandomOrder(Arrays.asList("Black"));
    }
}
