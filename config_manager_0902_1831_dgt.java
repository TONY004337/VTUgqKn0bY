// 代码生成时间: 2025-09-02 18:31:26
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

public class ConfigManager {

    private static final String CONFIG_FILE_PATH = "config.properties";
    private Properties config;

    /**
     * Constructor that initializes the ConfigManager with a properties file.
     */
    public ConfigManager() {
        config = new Properties();
        try {
            config.load(ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE_PATH, e);
        }
    }

    /**
     * Retrieves a configuration value by key.
     * 
     * @param key The key of the configuration value to retrieve.
     * @return The configuration value associated with the given key.
     */
    public String getConfigValue(String key) {
        return config.getProperty(key);
    }

    /**
     * Updates a configuration value by key.
     * 
     * @param key The key of the configuration value to update.
     * @param value The new value to set for the given key.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateConfigValue(String key, String value) {
        config.setProperty(key, value);
        try {
            config.store(ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH), null);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to update configuration file: " + CONFIG_FILE_PATH, e);
        }
    }

    /**
     * Starts the Javalin server and sets up the routes for configuration management.
     * 
     * @param port The port on which the server will listen.
     * @param configManager A reference to the ConfigManager instance.
     */
    public static void startServer(int port, ConfigManager configManager) {
        Javalin app = Javalin.create().start(port);
        app.routes(() -> {
            ApiBuilder.get("/config/:key", ctx -> {
                String key = ctx.pathParam("key");
                String value = configManager.getConfigValue(key);
                ctx.result(value);
            });
            ApiBuilder.put("/config/:key", ctx -> {
                String key = ctx.pathParam("key");
                String value = ctx.bodyAsClass(String.class);
                boolean updated = configManager.updateConfigValue(key, value);
                if (updated) {
                    ctx.status(200).result("Configuration updated successfully");
                } else {
                    ctx.status(500).result("Failed to update configuration");
                }
            });
        });
    }
}
