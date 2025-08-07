// 代码生成时间: 2025-08-08 07:32:31
import io.javalin.Javalin;
import io.javalin.core.util.Header;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public class XssProtection {

    // Pattern for detecting potential XSS attacks
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(<|%)[^>]+(>|%)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        // Define a route for a simple form submission
        app.post("/submit", ctx -> {
            HttpServletRequest request = ctx.req.req;
            String userInput = request.getParameter("userInput");

            // Sanitize user input to prevent XSS attacks
            String sanitizedInput = sanitizeInput(userInput);

            // Respond with sanitized input to demonstrate protection
            ctx.result(sanitizedInput);
        });

        app.options("/**", ctx -> ctx.result(200)); // Handle preflight CORS requests
    }

    /**
     * Sanitizes the input to remove potential XSS threats.
     * @param input The user input to sanitize.
     * @return The sanitized input.
     */
    private static String sanitizeInput(String input) {
        if (input == null) return "";

        // Escape HTML entities
        String escapedInput = input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace(""", "&quot;")
            .replace("'", "&#x27;");

        // Check for potential XSS patterns and throw an exception if found
        if (XSS_PATTERN.matcher(escapedInput).find()) {
            throw new IllegalArgumentException("Potential XSS attack detected!");
        }

        return escapedInput;
    }
}
