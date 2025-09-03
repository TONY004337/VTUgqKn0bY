// 代码生成时间: 2025-09-03 20:14:35
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import java.util.Random;

/**
 * TestDataGenerator is a Javalin application that generates random test data.
 */
public class TestDataGenerator {

    private static final int PORT = 7000;
    private static final Random random = new Random();

    /**
     * Main method to start the Javalin server.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(PORT);

        // Define routes
        app.routes(() -> {
            ApiBuilder.get("/test-data", TestDataGenerator::generateTestData);
        });
    }

    /**
     * Generates and returns a JSON object containing random test data.
     * @return A JSON object with random test data.
     */
    private static String generateTestData() {
        int id = random.nextInt(10000);
        String name = "User" + id;
        int age = 18 + random.nextInt(65);
        boolean isActive = random.nextBoolean();

        // Constructing JSON object with random data
        String data = "{
  "id": " + id + ",
  "name": "" + name + "",
  "age": " + age + ",
  "isActive": " + isActive + "
}";

        return data;
    }
}
