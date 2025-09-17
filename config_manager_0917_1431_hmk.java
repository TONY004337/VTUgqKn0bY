// 代码生成时间: 2025-09-17 14:31:12
import io.javalin.Javalin;
import io.javalin.core.util.Header;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * ConfigManager is a simple configuration file manager using Javalin framework.
 * It allows to retrieve and update configuration settings via HTTP endpoints.
 */
public class ConfigManager {

    private final String configFilePath;
    private final Properties properties;

    public ConfigManager(String configFilePath) throws IOException {
        this.configFilePath = configFilePath;
        this.properties = loadPropertiesFromFile(configFilePath);
    }

    /**
     * Starts the Javalin app with the configured routes for managing the configuration.
     */
    public void start() {
        Javalin app = Javalin.create().start(7000);
        app.get("/config", this::getConfig);
        app.put("/config", this::updateConfig);
    }

    /**
     * Loads properties from the file.
     *
     * @return The loaded properties.
     */
    private Properties loadPropertiesFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Properties properties = new Properties();
        if (Files.exists(path)) {
            properties.load(Files.newInputStream(path));
        } else {
            throw new IOException("Configuration file not found: " + filePath);
        }
        return properties;
    }

    /**
     * HTTP GET endpoint to retrieve the current configuration.
     */
    private void getConfig() {
        Map<String, String> configAsMap = properties.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().toString()
                ));

        // Return the configuration as JSON
        // Note: Javalin automatically converts Map to JSON
        configAsMap.forEach((key, value) -> ctx.res().status(200).result(Map.of(key, value)));
    }

    /**
     * HTTP PUT endpoint to update the configuration.
     */
    private void updateConfig() {
        String body = ctx.bodyAsClass(String.class);
        try {
            Properties updatedProperties = new Properties();
            updatedProperties.load(java.nio.charset.StandardCharsets.UTF_8.newDecoder().decode(ctx.bodyInputStream()));

            // Update the properties in memory
            properties.putAll(updatedProperties);
            // Persist the changes to the file
            Files.write(Paths.get(configFilePath),
                    properties.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));

            ctx.res().status(200).result("Configuration updated successfully.");
        } catch (IOException e) {
            ctx.res().status(500).result("Failed to update configuration: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            new ConfigManager("config.properties").start();
        } catch (IOException e) {
            System.err.println("Failed to start ConfigManager: " + e.getMessage());
        }
    }
}
