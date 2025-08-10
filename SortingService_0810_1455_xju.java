// 代码生成时间: 2025-08-10 14:55:53
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Javalin 框架用于创建 Web 服务
import io.javalin.Javalin;

public class SortingService {

    // 构造函数，创建 Javalin 实例
    public SortingService(int port) {
        Javalin app = Javalin.create().start(port);
        app.get("/sort", ctx -> ctx.json(sortNumbers()));
    }

    // 用于排序数字列表的方法
    private List<Integer> sortNumbers() {
        try {
            List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5);
            Collections.sort(numbers);
            return numbers;
        } catch (Exception e) {
            // 错误处理，将错误信息返回给客户端
            throw new RuntimeException("Error occurred while sorting numbers", e);
        }
    }

    // 主函数，程序的入口点
    public static void main(String[] args) {
        // 假设端口号为 8080
        int port = 8080;
        new SortingService(port);
    }
}
