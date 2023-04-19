package ru.practikum;

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
}
