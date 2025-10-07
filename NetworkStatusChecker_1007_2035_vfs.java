// 代码生成时间: 2025-10-07 20:35:43
import io.javalin.Javalin;
import io.javalin.http.Handler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class NetworkStatusChecker {

    // Define the port number for the Javalin server
    private static final int PORT = 7000;

    public static void main(String[] args) {
        // Initialize the Javalin server
        Javalin app = Javalin.create().start(PORT);

        // Define the handler for the /check route
        app.get("/check", new Handler() {
            @Override
            public void handle(Context context) {
                try {
                    // Check the network connection status
                    boolean isConnected = checkNetworkConnection();

                    // Return the result as a JSON response
                    context.json(isConnected);
                } catch (IOException e) {
                    // Handle exceptions and return an error message
                    context.status(500).json("Error: Unable to check network connection.");
                }
            }
        });
    }

    // Method to check the network connection status
    private static boolean checkNetworkConnection() throws IOException {
        // Try to connect to a reliable URL (e.g., Google)
        URL url = new URL("http://www.google.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // Check the HTTP response code
        int responseCode = connection.getResponseCode();
        connection.disconnect();

        // Return true if the response code is 200 (OK), false otherwise
        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
