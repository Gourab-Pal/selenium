package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    private TestConfig() {
    }

    public static String getBrowser() {
        return envOrProperty("BROWSER", "browser", "chrome");
    }

    public static boolean isHeadless() {
        String value = envOrProperty("HEADLESS", "headless", "false");
        return Boolean.parseBoolean(value);
    }

    public static String getTheInternetBaseUrl() {
        return PROPERTIES.getProperty("base.url.theinternet");
    }

    public static String getSauceDemoBaseUrl() {
        return PROPERTIES.getProperty("base.url.saucedemo");
    }

    public static int getImplicitWaitSeconds() {
        return Integer.parseInt(PROPERTIES.getProperty("implicit.wait.seconds", "0"));
    }

    public static int getExplicitWaitSeconds() {
        return Integer.parseInt(PROPERTIES.getProperty("explicit.wait.seconds", "10"));
    }

    private static String envOrProperty(String envKey, String propertyKey, String defaultValue) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }
        return PROPERTIES.getProperty(propertyKey, defaultValue);
    }
}
