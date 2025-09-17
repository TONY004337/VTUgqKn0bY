// 代码生成时间: 2025-09-17 09:33:32
import java.util.Random;
import io.javalin.Javalin;
import org.json.JSONObject;

/**
 * 测试数据生成器，使用JAVA和JAVALIN框架创建一个简单的REST API端点。
 * 生成随机数据作为测试数据。
 */
public class TestDataGenerator {

    private static final Random random = new Random();
    private static final String[] adjectives = { "quick", "lazy", "playful", "cute", "fierce", "sly", "daring", "clever", "mild", "meek" };
    private static final String[] animals = { "fox", "dog", "cat", "lion", "tiger", "bear", "wolf", "cheetah", "elephant", "giraffe" };

    /**
     * 创建并启动Javalin服务器。
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        // 创建测试数据生成器端点
        app.get("/generate", ctx -> {
            try {
                // 使用'/'作为JSON对象的键，因为JSON对象的键必须是字符串
                ctx.json(new JSONObject(generateTestData()));
            } catch (Exception e) {
                // 错误处理
                ctx.status(500).result("Error generating test data: " + e.getMessage());
            }
        });
    }

    /**
     * 生成随机测试数据。
     * @return 包含随机测试数据的JSON对象。
     */
    private static JSONObject generateTestData() {
        JSONObject testData = new JSONObject();

        // 添加随机生成的字符串
        testData.put("name", adjectives[random.nextInt(adjectives.length)] + " " + animals[random.nextInt(animals.length)]);
        // 添加随机生成的整数
        testData.put("age", random.nextInt(100));
        // 添加随机生成的布尔值
        testData.put("is_active", random.nextBoolean());

        return testData;
    }
}
