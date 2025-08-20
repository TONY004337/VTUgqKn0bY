// 代码生成时间: 2025-08-20 21:52:52
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MathUtilityTool {

    // Define Javalin HTTP server
    private static final Javalin app = Javalin.create().start(7000);

    // Define a map to hold the mathematical operations
    private static final Map<String, BiFunction<Double, Double, Double>> operations = new HashMap<>();

    static {
        // Initialize the operations map with basic mathematical operations
        operations.put("add", (a, b) -> a + b);
        operations.put("subtract", (a, b) -> a - b);
        operations.put("multiply", (a, b) -> a * b);
        operations.put("divide", (a, b) -> {
            if (b == 0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
            return a / b;
        });
    }

    // Main method to start the application
    public static void main(String[] args) {
        // Define routes for each operation
        getOperations().forEach((name, operation) -> {
            app.get("/" + name, ctx -> {
                try {
                    double a = ctx.queryDouble("a").orElse(0);
                    double b = ctx.queryDouble("b").orElse(0);
                    double result = operation.apply(a, b);
                    ctx.json(toJsonMap(name, a, b, result));
                } catch (IllegalArgumentException e) {
                    ctx.status(400).json(toJsonMap(name, e.getMessage()));
                }
            });
        });
    }

    // Helper method to convert operation details to JSON format
    private static Map<String, Object> toJsonMap(String operationName, double a, double b, double result) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("operation", operationName);
        resultMap.put("a", a);
        resultMap.put("b", b);
        resultMap.put("result", result);
        return resultMap;
    }

    // Helper method to convert error message to JSON format
    private static Map<String, String> toJsonMap(String operationName, String errorMessage) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("operation", operationName);
        resultMap.put("error", errorMessage);
        return resultMap;
    }

    // Helper method to get the map of operations
    private static Map<String, BiFunction<Double, Double, Double>> getOperations() {
        return operations;
    }
}
