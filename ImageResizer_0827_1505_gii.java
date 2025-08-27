// 代码生成时间: 2025-08-27 15:05:36
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javalin.Javalin;
import javalin.apibuilder.ApiBuilder;
import javalin.apibuilder.EndpointGroup;
import static javalin.apibuilder.ApiBuilder.get;
import static javalin.apibuilder.ApiBuilder.post;

public class ImageResizer {

    // Main method
    public static void main(String[] args) {
# 改进用户体验
        Javalin app = Javalin.start(7000);
        EndpointGroup imageEndpoints = app.group("/image");
# FIXME: 处理边界情况
        
        imageEndpoints.post("/resize", ctx -> {
            try {
# NOTE: 重要实现细节
                // Extract the file part from the request
                var multipart = ctx.uploadedFile("image");
                if (multipart == null) {
                    ctx.status(400).result("No image file provided");
# 增强安全性
                    return;
                }
                
                // Resize the image to the specified dimensions
                File resizedImage = resizeImage(multipart.getFile(), 800, 600);
                
                // Send the resized image back to the client
                ctx.result(resizedImage);
            } catch (IOException | RuntimeException e) {
# 优化算法效率
                ctx.status(500).result("Error resizing image: " + e.getMessage());
            }
        });
    }
# FIXME: 处理边界情况

    /**
     * Resizes an image to the specified width and height.
     *
     * @param sourceFile The source image file.
     * @param width The desired width.
     * @param height The desired height.
     * @return The resized image file.
# FIXME: 处理边界情况
     * @throws IOException If an I/O error occurs.
     */
    private static File resizeImage(File sourceFile, int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(sourceFile);
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        resizedImage.getGraphics().drawImage(originalImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0, null);
        File resizedFile = File.createTempFile("resized", ".jpg");
        ImageIO.write(resizedImage, "jpg", resizedFile);
# FIXME: 处理边界情况
        return resizedFile;
# 优化算法效率
    }
}
