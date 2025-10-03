package io.github.sinuscosinustan.hetznercloud.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {

    private static final String VERSION_PROPERTIES = "/version.properties";
    private static final String DEFAULT_VERSION = "unknown";

    private static String cachedVersion;

    public static String getLibraryVersion() {
        if (cachedVersion == null) {
            cachedVersion = loadVersion();
        }
        return cachedVersion;
    }

    private static String loadVersion() {
        try (InputStream input = VersionUtil.class.getResourceAsStream(VERSION_PROPERTIES)) {
            if (input == null) {
                return DEFAULT_VERSION;
            }

            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty("library.version", DEFAULT_VERSION);
        } catch (IOException e) {
            return DEFAULT_VERSION;
        }
    }
}