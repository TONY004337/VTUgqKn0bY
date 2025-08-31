// 代码生成时间: 2025-08-31 13:42:32
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.core.security.SecurityUtil;
import io.javalin.core.security.RouteRole;

import java.util.Set;
import java.util.HashSet;

public class AccessControl {

    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);

        // Define roles
        Set<RouteRole> adminRoles = new HashSet<>();
        adminRoles.add(SecurityUtil.roles("admin"));

        Set<RouteRole> userRoles = new HashSet<>();
        userRoles.add(SecurityUtil.roles("user"));

        // Secured routes
        app.routes(() -> {
            // Admin route
            get("/admin", ctx -> {
                if (!SecurityUtil.isAuthenticated(ctx)) {
                    ctx.status(401).result("Unauthorized");
                    return;
                }

                if (!adminRoles.contains(SecurityUtil.getRole(ctx))) {
                    ctx.status(403).result("Forbidden");
                    return;
                }

                ctx.result("Welcome to admin dashboard");
            }, adminRoles);

            // User route
            get("/user", ctx -> {
                if (!SecurityUtil.isAuthenticated(ctx)) {
                    ctx.status(401).result("Unauthorized");
                    return;
                }

                if (!userRoles.contains(SecurityUtil.getRole(ctx))) {
                    ctx.status(403).result("Forbidden");
                    return;
                }

                ctx.result("Welcome to user dashboard");
            }, userRoles);
        });
    }
}
