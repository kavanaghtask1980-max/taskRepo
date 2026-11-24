package config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties envProps = new Properties();
    private static final Properties targetEnvProps = new Properties();
    private static final Properties locatorProps = new Properties();
    private static final Properties secretProps = new Properties();
    private static final String activeEnv;

    static {
        try {
            // 1. Load global execution toggles (env.properties)
            try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream("configs/env.properties")) {
                if (is != null) {
                    envProps.load(is);
                }
            }

            // 2. Load UI element locators (locators.properties)
            try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream("configs/locators.properties")) {
                if (is != null) {
                    locatorProps.load(is);
                }
            }

            // 3. Resolve active environment: CLI (-Denv=qa) -> OS Env (ENV) -> env.properties -> default "qa"
            String resolvedEnv = System.getProperty("env");
            if (resolvedEnv == null || resolvedEnv.isBlank()) {
                resolvedEnv = System.getenv("ENV");
            }
            if (resolvedEnv == null || resolvedEnv.isBlank()) {
                resolvedEnv = envProps.getProperty("env", "qa");
            }
            activeEnv = resolvedEnv.trim().toLowerCase();
            System.out.println("[ConfigManager] Active Environment => " + activeEnv.toUpperCase());

            // 4. Load environment-specific properties (configs/qa.properties, staging.properties, prod.properties)
            String envFilePath = "configs/" + activeEnv + ".properties";
            try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(envFilePath)) {
                if (is != null) {
                    targetEnvProps.load(is);
                } else {
                    throw new RuntimeException("Environment config file not found on classpath: " + envFilePath);
                }
            }

            // 5. Load optional local gitignored secrets (configs/secrets.properties)
            java.io.File secretFile = new java.io.File("src/test/resources/configs/secrets.properties");
            if (secretFile.exists()) {
                try (FileInputStream fis = new FileInputStream(secretFile)) {
                    secretProps.load(fis);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ConfigManager", e);
        }
    }

    public static String getActiveEnv() {
        return activeEnv;
    }

    public static String getLocator(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }
        String loc = locatorProps.getProperty(key);
        if (loc != null) {
            return loc;
        }
        return envProps.getProperty(key);
    }

    public static String getGlobal(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }
        return envProps.getProperty(key);
    }

    public static String getEnv(String key) {
        // Priority 1: JVM system property (-Dkey=val)
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }

        // Priority 2: OS / Jenkins CI Environment Variable (exact or UPPER_SNAKE_CASE)
        String envVar = System.getenv(key);
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }
        String snakeKey = key.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase();
        String snakeEnvVar = System.getenv(snakeKey);
        if (snakeEnvVar != null && !snakeEnvVar.isBlank()) {
            return snakeEnvVar;
        }

        // Priority 3: Local gitignored secrets.properties
        String secretVal = secretProps.getProperty(key);
        if (secretVal != null && !secretVal.isBlank()) {
            return secretVal;
        }

        // Priority 4: Active environment properties (qa.properties / staging.properties / prod.properties)
        String val = targetEnvProps.getProperty(key);
        if (val != null && val.startsWith("${") && val.endsWith("}")) {
            return null;
        }
        return val;
    }
}