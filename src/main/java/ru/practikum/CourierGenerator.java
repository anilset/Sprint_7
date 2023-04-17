package ru.practikum;

public class CourierGenerator {
    public static Courier getRandomCourier() {
        return new Courier().setLogin(new Utilities().nextLogin)
                .setPassword(new Utilities().nextPw)
                .setFirstName(new Utilities().nextFirstName);
    }
}
