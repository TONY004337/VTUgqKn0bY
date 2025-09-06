// 代码生成时间: 2025-09-06 16:53:52
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * WebPageFetcher is a Java application using Javalin framework that provides a web page content fetching service.
 * It demonstrates the usage of Javalin for creating REST API and Jsoup for parsing HTML content.
 */
public class WebPageFetcher {

    /**
     * Main method to start the Javalin server.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000); // Start server on port 7000

        // Define a route to fetch and display web page content
        app.get("/fetch", ctx -> {
            String url = ctx.queryParam("url"); // Get URL from query parameter
            if (url == null || url.isEmpty()) {
                ctx.status(400).result("URL parameter is missing or empty"); // Bad request if URL is missing
                return;
            }

            try {
                Document document = Jsoup.connect(url).get(); // Fetch the web page content
                Elements content = document.select("body"); // Select body content
                ctx.result(content.html()); // Return the HTML content of the body
            } catch (IOException e) {
                ctx.status(500).result("Failed to fetch web page: " + e.getMessage()); // Server error if fetching fails
            }
        });
    }
}
