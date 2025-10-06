// 代码生成时间: 2025-10-06 21:21:49
import io.javalin.Javalin;
import io.javalin.core.util路线.Head火炬;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiTestingTool {

    /*
     * Initialize the Javalin app and start the server.
     *
     * @param args Command line arguments (not used in this case)
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000); // Start server on port 7000

        // Define API endpoints
        app.get("/ping", ctx -> ctx.result("Pong")); // Simple ping endpoint

        app.post("/test-api", new Handler<Context>() {
            @Override
            public void handle(Context ctx) {
                // Extract request data
                Map<String, String> requestData = ctx.formParamMap();
                String url = requestData.get("url");
                String method = requestData.get("method");
                Map<String, String> headers = new HashMap<>();
                requestData.forEach((key, value) -> {
                    if (key.startsWith("header_")) {
                        headers.put(key.replace("header_", ""), value);
                    }
                });

                // Validate request data
                if (url == null || url.trim().isEmpty()) {
                    ctx.status(HttpCode.BAD_REQUEST).result("URL is required");
                    return;
                }
                if (method == null || method.trim().isEmpty()) {
                    ctx.status(HttpCode.BAD_REQUEST).result("Method is required");
                    return;
                }

                try {
                    // Create and send HTTP request
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .method(method, HttpRequest.BodyPublishers.noBody())
                        .build();

                    // Add custom headers
                    headers.forEach(request::header);

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // Return response data
                    ctx.status(response.statusCode())
                        .json(Map.of(
                            "status", response.statusCode(),
                            "headers", response.headers().map().entrySet(),
                            "body", response.body()
                        ));
                } catch (Exception e) {
                    // Handle errors
                    ctx.status(HttpCode.INTERNAL_SERVER_ERROR).result("errors", Map.of("message", e.getMessage()));
                }
            }
        });
    }
}
