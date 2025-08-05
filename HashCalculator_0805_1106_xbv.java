// 代码生成时间: 2025-08-05 11:06:18
 * This program calculates hash values for given input strings.
 *
 * @author Your Name
 * @version 1.0
 */
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashCalculator {

    private static final String HASH_ALGORITHM = "SHA-256";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.get("/hash", ctx -> {
            try {
                String input = ctx.queryParam("input");
                if (input == null || input.trim().isEmpty()) {
                    throw new IllegalArgumentException("Input string cannot be empty");
                }
                String hash = calculateHash(input);
                ctx.json(hash);
            } catch (Exception e) {
                ctx.status(400);
                ctx.result(e.getMessage());
            }
        });
    }

    /**
     * Calculates the hash value of the given input using the specified hash algorithm.
     *
     * @param input The input string to calculate the hash for.
     * @return The hash value as a Base64 encoded string.
     * @throws NoSuchAlgorithmException If the specified hash algorithm is not available.
     */
    private static String calculateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] hashBytes = digest.digest(input.getBytes());
        // Convert hash bytes to Base64 encoded string
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
