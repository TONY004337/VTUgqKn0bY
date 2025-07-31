// 代码生成时间: 2025-07-31 15:30:25
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * A Javalin-based web application that provides a hash calculation tool.
 */
public class HashCalculator {

    private static final String HMAC_SHA_256 = "HmacSHA256";
    private static final String INVALID_INPUT_ERROR = "Invalid input. Please provide a string to hash.";

    /**
     * Main method to start the Javalin server.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.routes(() -> {
            // Define the API endpoint for generating a hash
            ApiBuilder.get("/hash", HashCalculator::handleHashRequest);
        });
    }

    /**
     * Handles the hash request by generating a HmacSHA256 hash of the input string.
     * @param ctx The Javalin context object.
     */
    private static void handleHashRequest(Context ctx) {
        String input = ctx.queryParam("input");
        String secretKey = ctx.queryParam("key");

        if (input == null || input.isEmpty() || secretKey == null || secretKey.isEmpty()) {
            ctx.status(400).result(INVALID_INPUT_ERROR);
            return;
        }

        try {
            byte[] keyBytes = secretKey.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, HMAC_SHA_256);
            Mac mac = Mac.getInstance(HMAC_SHA_256);
            mac.init(keySpec);

            byte[] result = mac.doFinal(input.getBytes());
            String base64Result = Base64.getEncoder().encodeToString(result);

            // Send back the base64 encoded hash
            ctx.json(createResponse(base64Result));
        } catch (Exception e) {
            // Handle any exceptions that occur during the hashing process
            ctx.status(500).result("Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Creates a JSON response map with the hash value.
     * @param hash The computed hash value.
     * @return A map representing the JSON response.
     */
    private static Map<String, String> createResponse(String hash) {
        Map<String, String> response = new HashMap<>();
        response.put("hash", hash);
        return response;
    }
}
