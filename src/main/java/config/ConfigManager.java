package config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties baseProp = new Properties();
    private static final Properties envProp = new Properties();

    static {
        try {
            String resPath = System.getProperty("user.dir") + "/src/test/resources/";

            // 1. Load env.properties (locators + active env)
            baseProp.load(new FileInputStream(resPath + "env.properties"));

            // 2. Load prod.properties (or -Denv=xxx override)
            String activeEnv = System.getProperty("env", baseProp.getProperty("env", "prod"));
            envProp.load(new FileInputStream(resPath + activeEnv + ".properties"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties files", e);
        }
    }

    // Read from env.properties (locators, grid_run, env)
    public static String getLocator(String key) {
        return baseProp.getProperty(key);
    }

    // Read from prod.properties (url, browser, credentials)
    public static String getEnv(String key) {
        String sysVal = System.getProperty(key);
        if (sysVal != null) {
            return sysVal;
        }
        return envProp.getProperty(key);
    }
}
