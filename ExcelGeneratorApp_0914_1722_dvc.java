// 代码生成时间: 2025-09-14 17:22:50
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.get;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ExcelGeneratorApp {

    // 主方法，启动Javalin服务器
    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);

        // 获取生成Excel的路由
        app.get("/generate-excel", ctx -> {
            // 调用方法生成Excel并发送到客户端
            generateAndSendExcel(ctx);
        });
    }

    // 生成Excel并发送到客户端
    private static void generateAndSendExcel(JavalinContext ctx) {
        // 创建一个Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        workbook.createSheet("Generated Sheet");

        try {
            // 使用输出流将Excel写入响应
            OutputStream os = ctx.resultStream();
            workbook.write(os);
            // 关闭工作簿和输出流
            os.close();
            workbook.close();
        } catch (IOException e) {
            // 错误处理
            ctx.status(500).result("Error generating Excel: " + e.getMessage());
        }
    }
}
