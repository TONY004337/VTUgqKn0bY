// 代码生成时间: 2025-09-05 04:53:49
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

public class MemoryAnalysis {

    // Create an instance of Javalin
    private static final Javalin app = Javalin.create().start(7000); // Start the server on port 7000

    /**
     * Main method to start the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Define routes
        app.get("/memory", MemoryAnalysis::memoryUsageHandler);
    }

    /**
     * Handles the memory usage request.
     *
     * @param ctx The Javalin context.
     */
    private static void memoryUsageHandler(Context ctx) {
        try {
            // Get the memory MX bean
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

            // Get memory usage
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

            // Create a map to hold the memory usage data
            Map<String, Object> memoryData = new HashMap<>();

            // Populate the map with memory usage data
            memoryData.put("heapMemoryUsage", heapMemoryUsage.toString());
            memoryData.put("nonHeapMemoryUsage", nonHeapMemoryUsage.toString());

            // Return the memory usage data as JSON
            ctx.json(memoryData);
        } catch (Exception e) {
            // Handle any exceptions
            ctx.status(500).json("Error: " + e.getMessage());
        }
    }
}
