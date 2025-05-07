package com.example.haru.config;

import io.github.cdimascio.dotenv.Dotenv;

// Singleton class
public class AppConfig {
    private static AppConfig instance;

    // default settings
    private String defaultServerAddress;
    private int defaultServerPort;
    private String defaultAPIBaseUrl;
    private String defaultLoginEndpoint;
    private String defaultRegisterEndpoint;
    private int defaultTokenLifetimeMinutes;

    private AppConfig() {
        loadConfig();
    }

    // get instance method
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
            return instance;
        }
        return instance;
    }

    // load config from .env file
    private void loadConfig() {
        Dotenv envFile = Dotenv.load();
        defaultServerAddress = envFile.get("DEFAULT_SERVER_ADDRESS");
        defaultServerPort = Integer.parseInt(envFile.get("DEFAULT_SERVER_PORT"));
        defaultAPIBaseUrl = envFile.get("DEFAULT_API_BASE_URL");
        defaultLoginEndpoint = envFile.get("DEFAULT_LOGIN_ENDPOINT");
        defaultRegisterEndpoint = envFile.get("DEFAULT_REGISTER_ENDPOINT");
        defaultTokenLifetimeMinutes = Integer.parseInt(envFile.get("DEFAULT_TOKEN_LIFETIME_MINUTES"));
    }

    // getters and setters
    public String getDefaultServerAddress() {
        return this.defaultServerAddress;
    }

    public int getDefaultServerPort() {
        return this.defaultServerPort;
    }

    public String getDefaultAPIBaseUrl() {
        return this.defaultAPIBaseUrl;
    }

    public String getDefaultLoginEndpoint() {
        return this.defaultLoginEndpoint;
    }

    public String getDefaultRegisterEndPoint() {
        return this.defaultRegisterEndpoint;
    }

    public int getDefaultTokenLifetimeMinutes() {
        return this.defaultTokenLifetimeMinutes;
    }
}
