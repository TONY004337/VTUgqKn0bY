// 代码生成时间: 2025-09-15 01:44:43
import io.javalin.Javalin;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletResponse;

public class RandomNumberGenerator {
    // The port number on which the Javalin server will run
    private static final int PORT = 7000;

    // The Javalin app instance
    private static final Javalin app = Javalin.create().start(PORT);

    public static void main(String[] args) {
        // Define the route for generating a random number
        app.get("/generate", ctx -> {
            int randomNumber = generateRandomNumber();
            ctx.json(randomNumber);
        });

        // Handle any unhandled routes with a 404 error
        app.error(404, ctx -> {
            ctx.status(HttpServletResponse.SC_NOT_FOUND);
            ctx.json("Error: Endpoint not found");
        });
    }

    /**
     * Generates a random number between 1 and 100
     * 
     * @return int - The generated random number
     */
    private static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(100) + 1; // Ensures the number is between 1 and 100
    }

    /**
     * Stops the Javalin server gracefully
     */
    public static void stopServer() {
        app.stop();
    }
}
