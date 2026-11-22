package config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties envProps = new Properties();
    private static final Properties prodProps = new Properties();
    private static final Properties localSecrets = new Properties();

    static {
        String basePath = System.getProperty("user.dir") + "/src/test/resources/";
        try {
            try (InputStream envStream = new FileInputStream(basePath + "env.properties")) {
                envProps.load(envStream);
            }
            String activeEnv = System.getProperty("env", envProps.getProperty("env", "prod"));
            try (InputStream targetStream = new FileInputStream(basePath + activeEnv + ".properties")) {
                prodProps.load(targetStream);
            }
            // Optional local-only file (gitignored) for developer convenience
            java.io.File secretFile = new java.io.File(basePath + "secrets.properties");
            if (secretFile.exists()) {
                try (InputStream secretStream = new FileInputStream(secretFile)) {
                    localSecrets.load(secretStream);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ConfigManager properties", e);
        }
    }

    public static String getLocator(String key) {
        return System.getProperty(key, envProps.getProperty(key));
    }

    public static String getEnv(String key) {
        // 1. Check OS / Jenkins CI Environment Variable (e.g., JENKINS_TOKEN or jenkinsToken)
        String envVar = System.getenv(key);
        if (envVar != null && !envVar.isBlank()) return envVar;

        String upperSnake = key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
        String envVarUpper = System.getenv(upperSnake);
        if (envVarUpper != null && !envVarUpper.isBlank()) return envVarUpper;

        // 2. Check Maven CLI -D flag
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) return sysProp;

        // 3. Check gitignored local secrets.properties
        if (localSecrets.containsKey(key)) return localSecrets.getProperty(key);

        // 4. Fallback to environment properties file (non-sensitive defaults)
        return prodProps.getProperty(key);
    }
}
