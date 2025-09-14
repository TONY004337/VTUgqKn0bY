// 代码生成时间: 2025-09-14 20:54:19
import io.javalin.Javalin;
import io.javalin.api.builder.ApiBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
# 扩展功能模块

public class MathCalculator {

    // 定义一个Javalin应用
    private static final Javalin app = Javalin.create().start(7000);

    // 定义一个包含数学运算的映射
    private static final Map<String, BiFunction<Double, Double, Double>> operations = new HashMap<>();

    // 初始化运算映射
    static {
        operations.put("add", (a, b) -> a + b);
        operations.put("subtract", (a, b) -> a - b);
        operations.put("multiply", (a, b) -> a * b);
        operations.put("divide", (a, b) -> {
            if (b == 0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
# TODO: 优化性能
            return a / b;
        });
    }
# NOTE: 重要实现细节

    // 定义API路由和处理逻辑
    public static void main(String[] args) {
        ApiBuilder.routes(() -> {
            // GET请求，用于返回支持的操作列表
# 扩展功能模块
            app.get("/operations", ctx -> {
                ctx.json(operations.keySet());
            });

            // POST请求，用于执行数学运算
# FIXME: 处理边界情况
            app.post("/calculate", ctx -> {
                try {
                    String operation = ctx.bodyAsClass(RequestParams.class).operation;
                    double a = ctx.bodyAsClass(RequestParams.class).a;
                    double b = ctx.bodyAsClass(RequestParams.class).b;

                    if (!operations.containsKey(operation)) {
                        ctx.status(400);
                        ctx.json(""Invalid operation"");
                        return;
                    }

                    double result = operations.get(operation).apply(a, b);
                    ctx.json(result);
                } catch (Exception e) {
                    ctx.status(400);
                    ctx.json(e.getMessage());
                }
            });
# NOTE: 重要实现细节
        });
# TODO: 优化性能
    }
# NOTE: 重要实现细节

    // 定义请求参数类
    private static class RequestParams {
# NOTE: 重要实现细节
        public String operation;
        public double a;
        public double b;
    }
}
