// 代码生成时间: 2025-08-27 08:58:06
import io.javalin.Javalin;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiResponseFormatter {

    private static final String STATUS_KEY = "status";
    private static final String MESSAGE_KEY = "message";
    private static final String DATA_KEY = "data";
    private static final String ERROR_KEY = "error";

    // 初始化Javalin服务器
    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);

        // 定义API响应格式化的路由
        app.get("/format-response", ctx -> {
            try {
                // 获取请求参数
                String status = ctx.queryParam("status");
                String message = ctx.queryParam("message");
                String data = ctx.queryParam("data");
                String error = ctx.queryParam("error");

                // 构建响应对象
                Map<String, Object> response = new HashMap<>();
                response.put(STATUS_KEY, status);
                response.put(MESSAGE_KEY, message);

                // 根据是否有数据或错误来构建响应
                if (data != null) {
                    response.put(DATA_KEY, new JSONObject(data));
                } else if (error != null) {
                    response.put(ERROR_KEY, new JSONObject(error));
                }

                // 返回格式化的响应
                ctx.json(response);
            } catch (Exception e) {
                // 错误处理
                ctx.status(500).json(createErrorResponse("Internal Server Error", e.getMessage()));
            }
        });
    }

    // 创建错误响应的方法
    private static Map<String, Object> createErrorResponse(String message, String error) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS_KEY, "error");
        errorResponse.put(MESSAGE_KEY, message);
        errorResponse.put(ERROR_KEY, error);
        return errorResponse;
    }
}
