// 代码生成时间: 2025-09-10 10:26:14
import io.javalin.Javalin;
import io.javalin.core.security.XssFilter;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import spark.ResponseTransformer;

import static io.javalin.apibuilder.ApiBuilder.get;
import static spark.Spark.port;

/*
 * 这个程序使用JAVALIN框架和Jsoup库来防止XSS攻击。
 * 它定义了一个简单的HTTP服务器，监听端口7000。
 * 对所有通过的请求，都会使用XssFilter来清洗输入，以防止XSS攻击。
 */
public class XssProtectionApp {

    // 配置Jsoup的白名单，根据需要可以添加或删除允许的标签和属性
    private static final Whitelist WHITELIST = Whitelist.simpleText()
            .addTags("a", "b", "i", "em", "strong", "p", "ul", "ol", "li", "br")
            .addAttributes("a