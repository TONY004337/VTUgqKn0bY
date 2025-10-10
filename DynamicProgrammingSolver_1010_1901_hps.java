// 代码生成时间: 2025-10-10 19:01:43
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.core.util.RouteOverviewPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// 动态规划解决器
public class DynamicProgrammingSolver {

    // 动态规划中的斐波那契数列示例
    private static final int MAX_FIB = 100;
    private int[] fib = new int[MAX_FIB];

    public DynamicProgrammingSolver() {
        // 初始化斐波那契数列的前两个值
        fib[0] = 0;
        fib[1] = 1;

        // 使用动态规划方法填充斐波那契数列数组
        for (int i = 2; i < MAX_FIB; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
    }

    // 获取斐波那契数列的第n项的值
    public int fibonacci(int n) {
        if (n < 0 || n > MAX_FIB - 1) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
        return fib[n];
    }

    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);
        app.routes(() -> {
            // API端点 /fibonacci，接受GET请求和一个参数n，返回斐波那契数列的第n项
            get("/fibonacci", ctx -> {
                try (Scanner scanner = new Scanner(ctx.queryParam("n").get())) {
                    int n = scanner.nextInt();
                    DynamicProgrammingSolver solver = new DynamicProgrammingSolver();
                    int result = solver.fibonacci(n);
                    ctx.result(String.valueOf(result));
                } catch (Exception e) {
                    ctx.status(400).result("Invalid input or out of bounds.");
                }
            });
        });
        // 启动Javalin服务器
        new RouteOverviewPlugin("/