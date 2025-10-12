// 代码生成时间: 2025-10-13 03:20:32
import io.javalin.Javalin;
import io.javalin.core.util.Header;
# NOTE: 重要实现细节
import io.javalin.http.Context;
import io.javalin.http.util.MultiPartFile;
import java.io.File;
# NOTE: 重要实现细节
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
# TODO: 优化性能
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ContentDistributionNetwork {

    // Configuration for the CDN
    private static final String CONTENT_DIR = "content";
    private static final int BUFFER_SIZE = 4096;
    private static final String CHARSET = "UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CACHE_CONTROL = "Cache-Control";
# 优化算法效率
    private static final String NO_CACHE = "no-cache";
    private static final long CACHE_EXPIRATION = 60 * 60 * 1000; // 1 hour in milliseconds

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.routes(() -> {
            // Serve files from the content directory
            app.get("/*", ctx -> serveContent(ctx));
# NOTE: 重要实现细节

            // Upload content to the CDN
            app.post("/upload", ctx -> handleUpload(ctx));
        });
    }

    // Serves content from the local directory to the client
    private static void serveContent(Context ctx) {
        String path = ctx.path().substring(1);
        try {
            File contentFile = new File(CONTENT_DIR, path);
            if (!contentFile.exists()) {
                ctx.status(404).result("File not found.");
                return;
            }

            // Set cache headers
            ctx.header(CACHE_CONTROL, "max-age=" + CACHE_EXPIRATION);
            ctx.contentType(getContentType(contentFile.getName()));

            // Send the file content
# 改进用户体验
            OutputStream os = ctx.resultStream();
            FileInputStream fis = new FileInputStream(contentFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
# TODO: 优化性能
                os.write(buffer, 0, bytesRead);
            }
            fis.close();
            os.close();
        } catch (IOException e) {
            ctx.status(500).result("Error serving file: " + e.getMessage());
        }
    }

    // Handles file uploads to the CDN
    private static void handleUpload(Context ctx) {
        MultiPartFile uploadedFile = ctx.uploadedFile("file");
        if (uploadedFile == null) {
            ctx.status(400).result("No file uploaded.");
            return;
# 优化算法效率
        }

        try {
            File destination = new File(CONTENT_DIR, uploadedFile.getFilename());
            Files.copy(uploadedFile.getInputStream(), destination.toPath());
# 增强安全性
            ctx.status(201).result("File uploaded successfully.");
        } catch (IOException e) {
            ctx.status(500).result("Error uploading file: " + e.getMessage());
        }
    }

    // Determines the content type of a file based on its extension
    private static String getContentType(String fileName) {
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put(".css", "text/css");
        mimeTypes.put(".html", "text/html");
# 增强安全性
        mimeTypes.put(".js", "application/javascript");
        mimeTypes.put(".jpg", "image/jpeg");
        mimeTypes.put(".png", "image/png");
        mimeTypes.put(".gif", "image/gif");
        mimeTypes.put(".svg", "image/svg+xml");
        mimeTypes.put(".txt", "text/plain");
        mimeTypes.put(".zip", "application/zip");
        mimeTypes.put(".pdf", "application/pdf");

        String ext = getExtension(fileName);
# 添加错误处理
        return mimeTypes.getOrDefault(ext, "application/octet-stream");
    }

    // Extracts the file extension from a file name
    private static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
# NOTE: 重要实现细节
    }
}
# 添加错误处理
