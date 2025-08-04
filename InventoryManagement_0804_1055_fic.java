// 代码生成时间: 2025-08-04 10:55:18
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Inventory Management System using Javalin framework.
 */
public class InventoryManagement {

    private static final ConcurrentHashMap<Integer, String> inventory = new ConcurrentHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * Start the Javalin server with the inventory management API endpoints.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.routes(() -> {
            // Endpoint to add new inventory item
            post("/add", ctx -> {
                String item = ctx.bodyAsClass(String.class);
                int id = nextId.getAndIncrement();
                inventory.put(id, item);
                ctx.status(201).result("Item added with ID: " + id);
            }).consumes("text/plain");

            // Endpoint to get inventory item by ID
            get("/item/:id", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                String item = inventory.get(id);
                if (item == null) {
                    ctx.status(404).result("Item not found");
                } else {
                    ctx.result("Item ID: " + id + ", Item: " + item);
                }
            });

            // Endpoint to update inventory item by ID
            put("/item/:id", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                String item = ctx.bodyAsClass(String.class);
                if (inventory.replace(id, inventory.get(id), item) == null) {
                    ctx.status(404).result("Item not found");
                } else {
                    ctx.result("Item updated: " + item);
                }
            }).consumes("text/plain");

            // Endpoint to delete inventory item by ID
            delete("/item/:id", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                if (inventory.remove(id) == null) {
                    ctx.status(404).result("Item not found");
                } else {
                    ctx.result("Item deleted");
                }
            });
        });
    }
}
