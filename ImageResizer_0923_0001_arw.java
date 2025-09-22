// 代码生成时间: 2025-09-23 00:01:21
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.ContentType;
import org.apache.commons.io.FileUtils;
import org.by apache.commons.io.FilenameUtils;

public class ImageResizer {

    private static final String UPLOAD_DIRECTORY = "uploads";
    private static final String TEMP_DIRECTORY = "temp";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.routes(() -> {
            EndpointGroup imageRoutes = app.group("/images", () -> {
                app.post("/resize", ctx -> {
                    try {
                        resizeImages(ctx);
                    } catch (Exception e) {
                        ctx.status(500).result("Error resizing images: " + e.getMessage());
                    }
                });
            });
        });
    }

    private static void resizeImages(Context ctx) throws IOException {
        // 获取上传的文件
        String uploadedFile = ctx.formParam("file");
        if (uploadedFile == null || uploadedFile.isEmpty()) {
            ctx.status(400).result("No file uploaded.");
            return;
        }

        // 获取目标尺寸
        int targetWidth = Integer.parseInt(ctx.formParam("width"));
        int targetHeight = Integer.parseInt(ctx.formParam("height"));

        // 读取图片文件
        File imageFile = new File(UPLOAD_DIRECTORY + File.separator + uploadedFile);
        if (!imageFile.exists()) {
            ctx.status(404).result("File not found.");
            return;
        }

        // 读取图片内容
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            ctx.status(400).result("Invalid image file.");
            return;
        }

        // 计算新尺寸
        double aspectRatio = (double) targetWidth / targetHeight;
        int newWidth, newHeight;
        if (image.getWidth() > image.getHeight() * aspectRatio) {
            newWidth = (int) (image.getHeight() * aspectRatio);
            newHeight = image.getHeight();
        } else {
            newWidth = image.getWidth();
            newHeight = (int) (image.getWidth() / aspectRatio);
        }

        // 调整图片尺寸
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, null);

        // 保存调整后的图片
        String tempFileName = FilenameUtils.removeExtension(uploadedFile) + "_resized" + FilenameUtils.getExtension(uploadedFile);
        File tempFile = new File(TEMP_DIRECTORY + File.separator + tempFileName);
        ImageIO.write(resizedImage, FilenameUtils.getExtension(uploadedFile), tempFile);

        // 发送调整后的图片到客户端
        ctx.contentType(ContentType.JPEG);
        ctx.result(FileUtils.readFileToString(tempFile, "ISO-8859-1"));
    }
}
