// 代码生成时间: 2025-08-25 01:16:27
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class ImageResizer {

    // 主方法，用于启动Javalin服务器
    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);
        configureRoutes(app);
    }

    // 配置路由
    private static void configureRoutes(Javalin app) {
        app.post("/resize", ctx -> {
            List<File> resizedImages = resizeImages(ctx));
            ctx.json(resizedImages);
        });
    }

    // 处理图片尺寸调整的请求
    private static List<File> resizeImages(Javalin ctx) {
        List<File> resizedImages = new ArrayList<>();
        List<File> uploadedFiles = ctx.uploadedFiles("files");
        try {
            // 获取目标尺寸
            double targetWidth = ctx.bodyAsClass(ImageResizeRequest.class).getWidth();
            double targetHeight = ctx.bodyAsClass(ImageResizeRequest.class).getHeight();

            for (File uploadedFile : uploadedFiles) {
                // 调整图片尺寸
                File resizedFile = resizeImage(uploadedFile, targetWidth, targetHeight);
                resizedImages.add(resizedFile);
            }
        } catch (IOException e) {
            // 错误处理
            ctx.status(500);
            ctx.result("Error resizing images: " + e.getMessage());
        }
        return resizedImages;
    }

    // 调整单张图片的尺寸
    private static File resizeImage(File originalImage, double targetWidth, double targetHeight) throws IOException {
        BufferedImage original = ImageIO.read(originalImage);
        double aspectRatio = original.getWidth() / (double) original.getHeight();
        double targetAspectRatio = targetWidth / targetHeight;

        int newWidth, newHeight;
        if (Math.abs(1 - aspectRatio / targetAspectRatio) < 0.01) {
            // 保持图片比例不变
            newWidth = (int) targetWidth;
            newHeight = (int) targetHeight;
        } else {
            // 调整图片比例以适应目标尺寸
            if (aspectRatio > targetAspectRatio) {
                newWidth = (int) targetHeight * aspectRatio;
                newHeight = (int) targetHeight;
            } else {
                newWidth = (int) targetWidth;
                newHeight = (int) targetWidth / aspectRatio;
            }
        }

        // 创建新的BufferedImage对象
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        // 保存调整后的图片
        String resizedFileName = originalImage.getName() + "_resized";
        File resizedFile = new File(originalImage.getParent(), resizedFileName);
        ImageIO.write(resized, "png", resizedFile);
        return resizedFile;
    }
}

// 用于接收尺寸参数的类
class ImageResizeRequest {
    private double width;
    private double height;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}