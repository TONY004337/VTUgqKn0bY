// 代码生成时间: 2025-09-23 06:25:44
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;

public class WebContentCrawler {

    // 主方法，程序入口
    public static void main(String[] args) {
        // 创建Javalin服务器实例
        Javalin app = Javalin.create().configureStaticFiles(staticFiles -> {
            staticFiles.location(Location.CLASSPATH);
        }).start(7000); // 启动服务器，端口7000

        // 定义路由，用于网页内容抓取
        app.get("/crawl", ctx -> {
            try {
                // 从请求中获取网址参数
                String url = ctx.queryParam("url");

                // 检查URL是否为空
                if (url == null || url.isEmpty()) {
                    ctx.status(400);
                    ctx.result("URL parameter is missing or empty");
                    return;
                }

                // 使用Jsoup抓取网页内容
                Document doc = Jsoup.connect(url).get();

                // 将抓取的网页内容返回给客户端
                ctx.result(doc.toString());
            } catch (IOException e) {
                // 错误处理，返回错误信息
                ctx.status(500);
                ctx.result("Error occurred while crawling the webpage: " + e.getMessage());
            }
        });
    }
}
