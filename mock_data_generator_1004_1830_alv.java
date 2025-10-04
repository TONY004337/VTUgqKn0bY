// 代码生成时间: 2025-10-04 18:30:46
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Random;
import java.util.UUID;

public class MockDataGenerator {

    // 定义随机数生成器
    private static final Random random = new Random();

    // Mock数据的端口号
    private static final int MOCK_DATA_PORT = 7000;

    public static void main(String[] args) {
        // 初始化Javalin服务器
        Javalin app = Javalin.create().start(MOCK_DATA_PORT);

        // 注册生成随机字符串的路由
        app.get("/random-string", ctx -> ctx.result(randomString()));

        // 注册生成随机数字的路由
        app.get("/random-number", ctx -> ctx.result(String.valueOf(randomNumber())));

        // 注册生成随机UUID的路由
        app.get("/random-uuid", ctx -> ctx.result(randomUUID()));
    }

    // 生成随机字符串的方法
    // 长度参数允许指定生成的字符串长度
    private static String randomString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char character = (char) (random.nextInt(26) + 'a');
            builder.append(character);
        }
        return builder.toString();
    }

    // 生成随机字符串的快捷方法，长度默认为10
    private static String randomString() {
        return randomString(10);
    }

    // 生成随机数字的方法，范围从1到100
    private static int randomNumber() {
        return random.nextInt(100) + 1;
    }

    // 生成随机UUID的方法
    private static String randomUUID() {
        return UUID.randomUUID().toString();
    }
}
