// 代码生成时间: 2025-08-26 16:05:50
import io.javalin.Javalin;
import io.javalin.http.Handler;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class WebContentScraper {
    // Main method to start the Javalin server
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Javalin app = Javalin.start(7000);

        // Define the endpoint to fetch web content
        app.get("/fetch-content", new Handler() {
            @Override
            public void handle(Context ctx) throws IOException {
                String url = ctx.pathParam("url");
                if (url == null || url.isEmpty()) {
                    ctx.status(400).result("URL parameter is required.");
                    return;
                }

                try {
                    String content = fetchWebContent(url);
                    ctx.result(content);
                } catch (IOException e) {
                    ctx.status(500).result("Error fetching web content: " + e.getMessage());
                }
            }
        });
    }

    // Method to fetch web content from a given URL
    private static String fetchWebContent(String url) throws IOException {
        try (Scanner scanner = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8.name())) {
            scanner.useDelimiter("\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
