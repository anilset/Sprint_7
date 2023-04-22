package ru.practikum.courier_services;

import ru.practikum.Utilities;
import ru.practikum.pojo.Courier;

public class CourierGenerator {
    public static Courier getRandomCourier() {
        return new Courier().setLogin(new Utilities().nextLogin)
                .setPassword(new Utilities().nextPw)
                .setFirstName(new Utilities().nextFirstName);
    }
}
