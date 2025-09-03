// 代码生成时间: 2025-09-03 16:31:14
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.core.util.Header;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTestScript {

    private static final int NUM_REQUESTS = 100; // Number of requests to send
    private static final int THREAD_POOL_SIZE = 10; // Number of threads in the thread pool
    private static final int WARMUP_TIME = 5; // Warm-up time in seconds
    private static final int MEASUREMENT_TIME = 10; // Measurement time in seconds
    private static final int SERVER_PORT = 7000; // Port on which the server will listen
    private static final String SERVER_URL = "http://localhost:" + SERVER_PORT + "/test";

    public static void main(String[] args) {
        Javalin app = createServer();
        app.start(SERVER_PORT); // Start the server

        // Warm-up period to stabilize the server
        System.out.println("Warm-up period...");
        performRequests(WARMUP_TIME);

        // Measurement period to gather performance data
        System.out.println("Starting performance measurement...");
        long startTime = System.nanoTime();
        performRequests(MEASUREMENT_TIME);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Performance measurement completed.");
        System.out.println("Total requests: " + NUM_REQUESTS);
        System.out.println("Average response time: " + (duration / NUM_REQUESTS) + " nanoseconds");

        // Stop the server
        app.stop();
    }

    /**
     * Creates a Javalin server with a single test endpoint.
     * 
     * @return Javalin app instance.
     */
    private static Javalin createServer() {
        Javalin app = Javalin.create().configureEncoder();

        app.routes(() -> {
            ApiBuilder.get("/test", ctx -> {
                // Simple endpoint that returns a fixed response
                ctx.result("Test response");
            });
        });

        return app;
    }

    /**
     * Performs a specified number of HTTP GET requests to the server.
     * 
     * @param durationInSeconds Duration for which to perform requests in seconds.
     */
    private static void performRequests(int durationInSeconds) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(durationInSeconds);

        for (int i = 0; i < NUM_REQUESTS; i++) {
            executorService.execute(() -> {
                try {
                    URL url = new URL(SERVER_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new RuntimeException("Server returned non-OK status: " + responseCode);
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
