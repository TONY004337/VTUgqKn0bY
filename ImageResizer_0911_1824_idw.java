// 代码生成时间: 2025-09-11 18:24:38
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import io.javalin.Javalin;

public class ImageResizer {

    // 定义常量，便于管理
    private static final String IMAGES_DIRECTORY = "./images/"; // 存放图片的目录
    private static final String RESIZED_DIRECTORY = IMAGES_DIRECTORY + "resized/"; // 存放调整尺寸后的图片目录

    public static void main(String[] args) {
        Javalin app = Javalin.start(7000); // 启动Javalin服务，端口7000

        // 定义路由，接收POST请求，处理图像尺寸调整
        app.post("/resize", ctx -> {
            // 获取上传的图片文件
            String uploadedFile = ctx.uploadedFile("image").get().getFileName();
            File imageFile = new File(IMAGES_DIRECTORY + uploadedFile);

            // 获取图片的目标尺寸
            int width = Integer.parseInt(ctx.formParam("width"));
            int height = Integer.parseInt(ctx.formParam("height"));

            try {
                // 调整图片尺寸
                resizeImage(imageFile, width, height);
                ctx.result("Image resized successfully!");
            } catch (IOException e) {
                // 错误处理
                ctx.status(500).result("Error resizing image: " + e.getMessage());
            }
        });

        // 定义路由，返回调整尺寸后的图片
        app.get("/resized/:filename", ctx -> {
            String filename = ctx.pathParam("filename");
            String filePath = RESIZED_DIRECTORY + filename;

            // 检查文件是否存在
            if (new File(filePath).exists()) {
                ctx.result(filePath);
            } else {
                ctx.status(404).result("File not found");
            }
        });

        // 关闭Javalin服务
        // app.stop();
    }

    // 方法：调整图片尺寸
    private static void resizeImage(File imageFile, int width, int height) throws IOException {
        // 读取图片
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (originalImage == null) {
            throw new IOException("Error reading image file");
        }

        // 创建新的图像，并调整尺寸
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();

        // 保存调整尺寸后的图片
        String resizedFilename = getResizedFilename(imageFile.getName());
        File resizedFile = new File(RESIZED_DIRECTORY + resizedFilename);
        ImageIO.write(resizedImage, "jpg", resizedFile);
    }

    // 方法：获取调整尺寸后的图片文件名
    private static String getResizedFilename(String originalFilename) {
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        return filenameWithoutExtension + "_resized" + originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}