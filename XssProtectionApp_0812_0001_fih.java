// 代码生成时间: 2025-08-12 00:01:35
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.util.Header;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class XssProtectionApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.before(ctx -> xssProtection(ctx));
        app.get("/hello", ctx -> ctx.result("Hello, this is a protected page from XSS!"));
    }

    private static void xssProtection(Context ctx) {
        try {
            String body = ctx.body();
            if (body != null && !body.isEmpty()) {
                String sanitizedBody = Jsoup.clean(body, Safelist.basic());
                ctx.body(sanitizedBody);
            }
        } catch (Exception e) {
            System.err.println("Error while sanitizing input: " + e.getMessage());
            ctx.status(400);
            ctx.result("Invalid input, possible XSS attack detected.");
        }
    }
}
