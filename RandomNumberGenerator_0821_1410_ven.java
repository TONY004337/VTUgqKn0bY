// 代码生成时间: 2025-08-21 14:10:55
import io.javalin.Javalin;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static io.javalin.ApiBuilder.get;

public class RandomNumberGenerator {

    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 100; // Inclusive
    private static final String PATH = "/generate";
    private static final String PARAM_MIN = "min";
    private static final String PARAM_MAX = "max";

    /**
     * Main method to start the Javalin server.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.get(PATH, ctx -> {
            try {
                int min = getParam(ctx, PARAM_MIN, DEFAULT_MIN);
                int max = getParam(ctx, PARAM_MAX, DEFAULT_MAX);
                // Ensure min is less than max
                if (min >= max) {
                    ctx.status(400); // Bad Request
                    ctx.result("Invalid parameters: min must be less than max");
                } else {
                    // Generate random number and send it as response
                    int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
                    ctx.json("Random number: " + randomNumber);
                }
            } catch (Exception e) {
                // Handle any unexpected errors
                ctx.status(500); // Internal Server Error
                ctx.result("An error occurred: " + e.getMessage());
            }
        });
    }

    /**
     * Helper method to get a parameter from the query string or use a default value.
     *
     * @param ctx The Javalin context
     * @param paramName The name of the parameter
     * @param defaultValue The default value to use if the parameter is not present
     * @return The parameter value as an integer
     */
    private static int getParam(Javalin ctx, String paramName, int defaultValue) {
        String paramValue = ctx.queryParam(paramName);
        if (paramValue == null || paramValue.isEmpty()) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(paramValue);
            } catch (NumberFormatException e) {
                ctx.status(400); // Bad Request
                ctx.result("Invalid parameter format for " + paramName);
                return defaultValue; // Won't be reached, but required to avoid compilation error
            }
        }
    }
}
