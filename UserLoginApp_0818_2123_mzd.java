// 代码生成时间: 2025-08-18 21:23:56
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * UserLoginApp is a simple Java application using Javalin framework to demonstrate
 * a basic user login verification system.
 */
public class UserLoginApp {

    // Dummy user credentials for testing purposes
    private static final Map<String, String> users = new HashMap<>();
    static {
        users.put("user1", "password1");
        users.put("user2", "password2");
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        // Endpoint to handle user login
        app.post("/login", ctx -> {
            try {
                // Extract data from JSON body
                JSONObject requestBody = new JSONObject(ctx.bodyAsJson());
                String username = requestBody.getString("username");
                String password = requestBody.getString("password");

                // Validate credentials
                String storedPassword = users.get(username);
                if (storedPassword == null) {
                    ctx.status(401).result("User not found");
                } else if (!storedPassword.equals(password)) {
                    ctx.status(401).result("Invalid password");
                } else {
                    ctx.status(200).result("Login successful");
                }

            } catch (JSONException e) {
                // Handle JSON parsing errors
                ctx.status(400).result("Invalid JSON format");
            }
        });

        System.out.println("Server started on http://localhost:7000");
    }
}
