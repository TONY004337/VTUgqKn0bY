// 代码生成时间: 2025-08-29 17:39:03
import io.javalin.api.Handler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.util.Header;

import java.util.Base64;
import java.util.Optional;

public class UserAuthentication {

    private static final String BASIC_AUTH_PREFIX = "Basic ";
    private static final String USER = "admin";
    private static final String PASSWORD = "password123"; // In a real scenario, use a more secure way to store passwords, e.g., hashed passwords

    // Initializes Javalin app and sets up routes
    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);

        // Authentication route
        app.before(ctx -> authenticate(ctx));

        // Example secured route
        app.get("/secure", ctx -> ctx.result("Welcome to the secure area!"));
    }

    // Authentication middleware
    private static void authenticate(Context ctx) {
        Optional<String> authHeader = Optional.ofNullable(ctx.header(Header.AUTHORIZATION));

        if (authHeader.isPresent() && authHeader.get().startsWith(BASIC_AUTH_PREFIX)) {
            String encodedCredentials = authHeader.get().substring(BASIC_AUTH_PREFIX.length());
            String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
            String[] credentials = decodedCredentials.split(":");

            if (credentials.length == 2 && USER.equals(credentials[0]) && PASSWORD.equals(credentials[1])) {
                // Authentication successful
                return;
            }
        }

        // Authentication failed
        ctx.status(401).result("Unauthorized");
    }
}
