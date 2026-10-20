package com.swaglabs.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Properties;

/**
 * Reads framework settings. Each key is resolved in this order:
 * <ol>
 *     <li>JVM system property, e.g. -Dheadless=true</li>
 *     <li>environment variable, key upper-cased with dots as underscores, e.g. USER_PASSWORD</li>
 *     <li>config.properties on the classpath</li>
 * </ol>
 * Environment variables are how CI servers inject secrets such as credentials.
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getBrowser() {
        return get("browser");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static Duration getTimeout() {
        return Duration.ofSeconds(Long.parseLong(get("timeout.seconds")));
    }

    public static Duration getSlowMotion() {
        return Duration.ofMillis(Long.parseLong(get("slow.motion.ms")));
    }

    /**
     * Username for a configured user, read from user.&lt;name&gt;.username
     * (environment variable USER_&lt;NAME&gt;_USERNAME), e.g. getUsername("standard").
     */
    public static String getUsername(String user) {
        return get("user." + user + ".username");
    }

    public static String getPassword() {
        return get("user.password");
    }

    private static String get(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key.toUpperCase().replace('.', '_'));
        }
        if (value == null) {
            value = PROPERTIES.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing config value for '" + key + "'");
        }
        return value.trim();
    }

    private static Properties load() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found on the classpath");
            }
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + CONFIG_FILE, e);
        }
    }
}