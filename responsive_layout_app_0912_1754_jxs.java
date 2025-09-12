// 代码生成时间: 2025-09-12 17:54:04
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import spark.Spark;

import static spark.Spark.*;

/**
 * ResponsiveLayoutApp is a Javalin application that demonstrates a responsive layout design.
 */
public class ResponsiveLayoutApp {

    // Define the port where the application will run
    private static final int PORT = 7000;

    public static void main(String[] args) {
        // Initialize Javalin app
        Javalin app = Javalin.create().start(PORT);

        // Configure Thymeleaf template engine for rendering HTML
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateEngine.setTemplateResolver(templateResolver);

        // Define routes
        app.routes(() -> {
            // Home page route
            get(