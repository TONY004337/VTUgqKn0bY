// 代码生成时间: 2025-08-07 20:20:39
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.core.security.RouteRole;
import java.util.Map;
import java.util.function.Consumer;

public class AccessControlExample {

    public static void main(String[] args) {
        // 初始化Javalin应用
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(7000);

        // 定义用户角色
        final Map<String, RouteRole> roles = Map.of(
            "admin", RouteRole.ADMIN,
            "user", RouteRole.USER
        );

        // 添加错误处理
        app.error(400, exception -> {
            // 处理400 Bad Request错误
            System.out.println("400 Bad Request: " + exception.getMessage());
        });

        // 添加访问权限控制的路由
        app.routes(() -> {
            // 公开路由
            get("/public", ctx -> ctx.result("Public route accessible by anyone"));

            // 受保护的路由，仅管理员可以访问
            roles.keySet().forEach(role -> {
                get("/protected/" + role)
                    .roles(role)
                    .handler(ctx -> ctx.result("Protected route accessible by 