package ru.practikum;

import org.apache.commons.lang3.RandomStringUtils;
import java.time.LocalDate;

public class Utilities {
    public String nextFirstName = RandomStringUtils.randomAlphabetic(10);
    public String nextLogin = RandomStringUtils.randomAlphabetic(5) + RandomStringUtils.randomNumeric(5);
    public String nextPw = RandomStringUtils.randomAlphabetic(2) + RandomStringUtils.randomNumeric(6);
    public static final LocalDate CURRENT_DATE = LocalDate.now();
    public static final String TOMORROW = String.valueOf(CURRENT_DATE.plusDays(1));
}
