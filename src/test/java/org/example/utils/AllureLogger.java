package org.example.utils;

import io.qameta.allure.Allure;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AllureLogger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final long START_TIME = System.currentTimeMillis();

    public static void log(String message) {

        String timestamp =
                LocalDateTime.now().format(FORMATTER);

        Duration duration =
                Duration.ofMillis(System.currentTimeMillis() - START_TIME);

        long minutes = duration.toMinutes();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();

        String elapsed =
                String.format("+%02d:%02d.%03d",
                        minutes,
                        seconds,
                        millis);

        String logMessage =
                String.format("[%s] [%s] %s",
                        timestamp,
                        elapsed,
                        message);

        System.out.println(logMessage);
        Allure.step(logMessage);
    }
}