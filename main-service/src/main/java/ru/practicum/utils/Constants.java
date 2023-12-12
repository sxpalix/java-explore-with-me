package ru.practicum.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final LocalDateTime MIN_TIME = LocalDateTime.of(2001, 1, 1, 0, 0);
    public static final DateTimeFormatter DATE_TIME_FORMAT_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Constants() {
    }
}
