package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties envProps = new Properties();
    private static final Properties prodProps = new Properties();
    private static final Properties localSecrets = new Properties();

    static {
        String configDir = System.getProperty("user.dir") + "/src/test/resources/configs/";
        try {
            try (InputStream envStream = new FileInputStream(configDir + "env.properties")) {
                envProps.load(envStream);
            }
            String activeEnv = System.getProperty("env", envProps.getProperty("env", "prod"));
            try (InputStream targetStream = new FileInputStream(configDir + activeEnv + ".properties")) {
                prodProps.load(targetStream);
            }
            File secretFile = new File(configDir + "secrets.properties");
            if (secretFile.exists()) {
                try (InputStream secretStream = new FileInputStream(secretFile)) {
                    localSecrets.load(secretStream);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ConfigManager from " + configDir, e);
        }
    }

    public static String getLocator(String key) {
        return System.getProperty(key, envProps.getProperty(key));
    }

    public static String getEnv(String key) {
        String envVar = System.getenv(key);
        if (envVar != null && !envVar.isBlank()) return envVar;

        String upperSnake = key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
        String envVarUpper = System.getenv(upperSnake);
        if (envVarUpper != null && !envVarUpper.isBlank()) return envVarUpper;

        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) return sysProp;

        if (localSecrets.containsKey(key)) return localSecrets.getProperty(key);

        return prodProps.getProperty(key);
    }
}
