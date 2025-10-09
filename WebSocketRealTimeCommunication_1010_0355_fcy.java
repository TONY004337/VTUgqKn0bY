// 代码生成时间: 2025-10-10 03:55:21
import io.javalin.Javalin;
import io.javalin.websocket.WsHandler;
import io.javalin.websocket.WsMessage;
import io.javalin.websocket.WsPath;

public class WebSocketRealTimeCommunication {

    // Entry point for the application
    public static void main(String[] args) {
        // Initialize Javalin app
        Javalin app = Javalin.create().start(7000);

        // Define WebSocket paths and handlers
        app.ws(WsPath.ANY, new WsHandler() {
            @Override
            public void onConnect(WsMessage message) {
                // Handle new WebSocket connection
                System.out.println("New WebSocket connection established: " + message.sessionId());
            }

            @Override
            public void onMessage(WsMessage message) {
                // Handle incoming messages and broadcast to all connected clients
                System.out.println("Received message from " + message.sessionId() + ": " + message.text());
                // Broadcast message to all clients
                broadcast(message.text());
            }

            @Override
            public void onClose(WsMessage message) {
                // Handle WebSocket disconnection
                System.out.println("WebSocket connection closed: " + message.sessionId());
            }

            @Override
            public void onError(WsMessage message, Throwable throwable) {
                // Handle any errors that occur during WebSocket communication
                System.out.println("Error in WebSocket: " + message.sessionId() + " - " + throwable.getMessage());
            }
        });
    }

    // Helper method to broadcast a message to all connected clients
    private static void broadcast(String message) {
        // Get all connected WebSocket sessions
        WsPath.ANY.getSessions().forEach(session -> {
            try {
                // Send the message to each session
                session.send(message);
            } catch (Exception e) {
                // Handle any errors that occur during broadcasting
                System.out.println("Error broadcasting message: " + e.getMessage());
            }
        });
    }
}
