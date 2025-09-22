// 代码生成时间: 2025-09-22 14:57:34
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.http.Handler;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.staticfiles.StaticFileConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ShoppingCartApp is a web application built using Javalin framework to handle shopping cart functionalities.
 */
public class ShoppingCartApp {

    // In-memory storage for shopping cart items
    private static final Map<String, JSONArray> shoppingCarts = new ConcurrentHashMap<>();

    /**
     * Main method to start the Javalin server.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.routes(() -> {
            // Get shopping cart
            get("/cart/:userId", ctx -> {
                String userId = ctx.pathParam("userId");
                ctx.json(shoppingCarts.getOrDefault(userId, new JSONArray()));
            });

            // Add item to the shopping cart
            post("/cart/:userId", ctx -> {
                try {
                    String userId = ctx.pathParam("userId");
                    JSONObject item = new JSONObject(ctx.body());
                    JSONArray cart = shoppingCarts.getOrDefault(userId, new JSONArray());
                    cart.put(item);
                    shoppingCarts.put(userId, cart);
                    ctx.status(201).json(item);
                } catch (Exception e) {
                    ctx.status(400).result("Error adding item to cart: " + e.getMessage());
                }
            });

            // Remove item from the shopping cart
            delete("/cart/:userId/:itemId", ctx -> {
                try {
                    String userId = ctx.pathParam("userId");
                    String itemId = ctx.pathParam("itemId");
                    JSONArray cart = shoppingCarts.getOrDefault(userId, new JSONArray());
                    cart.removeIf(item -> item.toString().contains(itemId));
                    shoppingCarts.put(userId, cart);
                    ctx.status(204); // No content
                } catch (Exception e) {
                    ctx.status(400).result("Error removing item from cart: " + e.getMessage());
                }
            });

            // Clear the shopping cart
            delete("/cart/:userId", ctx -> {
                String userId = ctx.pathParam("userId");
                shoppingCarts.remove(userId);
                ctx.status(204); // No content
            });
        });

        // Setting up static files
        app.staticFiles=new StaticFileConfig[] {
                new StaticFileConfig.Builder("/js").location(Location.EXTERNAL).externalPath("./js").build(),
                new StaticFileConfig.Builder("/css").location(Location.EXTERNAL).externalPath("./css").build(),
                new StaticFileConfig.Builder("/").location(Location.EXTERNAL).externalPath("./html").build()
        };

    }

    /**
     * A simple method to simulate adding a shopping cart to the in-memory storage.
     * This is for demonstration purposes only and should be replaced with a proper database operation.
     * @param userId The ID of the user.
     * @return true if the cart was added successfully, false otherwise.
     */
    private static boolean addShoppingCart(String userId) {
        if (!shoppingCarts.containsKey(userId)) {
            shoppingCarts.put(userId, new JSONArray());
            return true;
        }
        return false;
    }
}
