// 代码生成时间: 2025-10-12 22:02:42
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.staticfiles.Location;
import java.util.Map;

/**
 * The RichTextEditorApp class sets up the Javalin server to host a simple
 * rich text editor.
 * 
 * @author YourName
 * @version 1.0
 */
public class RichTextEditorApp {

    /**
     * Main method to start the application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create()
                .configureStaticFiles(static -> {
                    static.location(Location.EXTERNAL);
                    static.path("/editor");
                    static.externalPath("src/main/resources/editor");
                })
                .start(7000); // Start the server on port 7000

        System.out.println("Server started on port 7000");
    }
}

// Place your HTML and JavaScript files for the rich text editor in the 'src/main/resources/editor' directory.
// This directory will be served statically by Javalin.