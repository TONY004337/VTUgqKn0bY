// 代码生成时间: 2025-07-31 03:30:51
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class JsonDataConverter {

    // 启动服务器的端口号
    private static final int PORT = 7000;

    // 启动Javalin服务器
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(PORT);

        // 设置HTTP路由来处理JSON数据转换请求
        app.post("/convert", JsonDataConverter::handleJsonConversion);
    }

    // 处理JSON数据转换的HTTP请求
    private static void handleJsonConversion(Context context) {
        try {
            // 从请求体中读取JSON字符串
            String requestBody = context.body();
            // 将JSON字符串解析为JSONObject对象
            JSONObject jsonObject = new JSONObject(requestBody);

            // 将JSONObject转换为Map对象
            Map<String, Object> convertedMap = convertJsonToMap(jsonObject);

            // 将Map对象转换回JSON字符串
            String convertedJson = new JSONObject(convertedMap).toString();

            // 设置响应内容类型为JSON
            context.contentType("application/json");
            // 返回转换后的JSON字符串
            context.result(convertedJson);
        } catch (JSONException e) {
            // 处理JSON解析错误
            context.status(400).result("Invalid JSON format");
        } catch (Exception e) {
            // 处理其他异常
            context.status(500).result("Internal Server Error");
        }
    }

    // 将JSONObject转换为Map对象
    private static Map<String, Object> convertJsonToMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        // 遍历JSONObject的键值对
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            // 如果值是JSONObject，递归转换
            if (value instanceof JSONObject) {
                map.put(key, convertJsonToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                // 如果值是JSONArray，转换为List并添加到Map中
                map.put(key, convertJsonArrayToList((JSONArray) value));
            } else {
                // 否则直接添加到Map中
                map.put(key, value);
            }
        }
        return map;
    }

    // 将JSONArray转换为List对象
    private static List<Object> convertJsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                // 如果值是JSONObject，递归转换
                list.add(convertJsonToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                // 如果值是JSONArray，递归转换
                list.add(convertJsonArrayToList((JSONArray) value));
            } else {
                // 否则直接添加到List中
                list.add(value);
            }
        }
        return list;
    }
}
