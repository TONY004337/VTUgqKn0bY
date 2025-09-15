// 代码生成时间: 2025-09-15 12:22:09
import io.javalin.Javalin;
import io.javalin.core.util.Header;
import org.apache.commons.io.IOUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTestScript {

    private static final int THREAD_COUNT = 50;
    private static final int REQUEST_COUNT = 1000;
    private static final String TEST_URL = "http://localhost:7000/performance";

    public static void main(String[] args) throws IOException, InterruptedException {

        // Start Javalin server
        Javalin app = Javalin.create().start(7000);

        // Define the endpoint for performance testing
        app.get("/performance", ctx -> ctx.result("Performance Test Endpoint"));

        // Create a thread pool to handle multiple requests
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        // Initialize counters for successful and failed requests
        int successfulRequests = 0;
        int failedRequests = 0;

        // Start the performance test
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < REQUEST_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    // Send HTTP request to the test endpoint
                    HttpURLConnection connection = (HttpURLConnection) new URL(TEST_URL).openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    // Check if the request was successful
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        successfulRequests++;
                    } else {
                        failedRequests++;
                    }

                    // Close the connection
                    connection.disconnect();
                } catch (IOException e) {
                    failedRequests++;
                }
            });
        }

        // Shutdown the executor service and wait for all tasks to complete
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        // Calculate the total time taken and print the results
        long endTime = System.currentTimeMillis();
        System.out.println("Total requests: " + REQUEST_COUNT);
        System.out.println("Successful requests: " + successfulRequests);
        System.out.println("Failed requests: " + failedRequests);
        System.out.println("Total time taken: " + (endTime - startTime) + "ms");

        // Stop the Javalin server
        app.stop();
    }
}
