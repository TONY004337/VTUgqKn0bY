// 代码生成时间: 2025-08-04 23:50:49
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.core.util.Header;
import org.apache.http.client.fluent.Request;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * PerformanceTestScript is a Javalin-based application designed for performance testing.
 * It creates a Javalin server and sends a specified number of concurrent HTTP requests
 * to test the performance of an endpoint.
 */
public class PerformanceTestScript {

    private static final int DEFAULT_PORT = 7000;
    private static final String TARGET_URL = "http://localhost:7000/";
    private static final int NUMBER_OF_THREADS = 100;
    private static final int NUMBER_OF_REQUESTS = 1000;

    public static void main(String[] args) {
        Javalin app = Javalin.start(Javalin.createConfig()
                .port(DEFAULT_PORT)
                .addStaticFiles("public"));

        // Define an endpoint for performance testing
        app.get("/test", ctx -> ctx.result("Performance Test Endpoint"));

        // Start the server
        try {
            app.start();
            System.out.println("Server started on port: " + DEFAULT_PORT);

            // Send concurrent HTTP requests to the performance test endpoint
            sendConcurrentRequests(TARGET_URL, NUMBER_OF_THREADS, NUMBER_OF_REQUESTS);

            // Keep the server running
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a specified number of concurrent HTTP requests to the given URL.
     *
     * @param url The URL to send requests to.
     * @param numberOfThreads The number of threads to use.
     * @param numberOfRequests The number of requests to send.
     */
    private static void sendConcurrentRequests(String url, int numberOfThreads, int numberOfRequests) {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfRequests; i++) {
            int finalI = i;
            executor.submit(() -> {
                try {
                    HttpResponse response = Request.Get(url + "/test").execute().returnResponse();
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Request " + finalI + ": " + responseBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
