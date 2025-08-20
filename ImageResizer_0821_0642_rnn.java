// 代码生成时间: 2025-08-21 06:42:26
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javalin.Javalin;
import javalin.apibuilder.ApiBuilder;
import javalin.apibuilder.CrudHandler;
import javalin.json.JSON;
import java.util.List;
import java.util.UUID;

public class ImageResizer {

    private static final String UPLOAD_DIRECTORY = "./uploads";
    private static final int MAX_IMAGE_SIZE = 1024; // Maximum image size in pixels
    private static final int NEW_WIDTH = 800;
    private static final int NEW_HEIGHT = 600;

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.post("/resize", ctx -> {
            // Check if the request contains files
            if (!ctx.contentType().equals("multipart/form-data")) {
                ctx.status(400);
                ctx.result("Invalid content type. Please use multipart/form-data.");
                return;
            }

            List<File> files = ctx.uploadedFiles("group");
            if (files.isEmpty()) {
                ctx.status(400);
                ctx.result("No files uploaded.");
                return;
            }

            try {
                for (File file : files) {
                    BufferedImage image = ImageIO.read(file);
                    if (image == null) {
                        throw new IOException("Cannot read image file: " + file.getName());
                    }

                    BufferedImage resizedImage = resizeImage(image);
                    File outputDir = new File(UPLOAD_DIRECTORY);
                    if (!outputDir.exists()) {
                        outputDir.mkdirs();
                    }

                    String outputFileName = UUID.randomUUID().toString() + "_resized." + getFileExtension(file.getName());
                    File outputFile = new File(UPLOAD_DIRECTORY, outputFileName);
                    ImageIO.write(resizedImage, getFileExtension(file.getName()), outputFile);
                }

                ctx.status(200);
                ctx.result("Images resized successfully.");
            } catch (IOException e) {
                ctx.status(500);
                ctx.result("Error resizing images: " + e.getMessage());
            }
        });
    }

    private static String getFileExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot > -1) {
            return fileName.substring(dot + 1);
        } else {
            return "";
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage) throws IOException {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        if (width > MAX_IMAGE_SIZE || height > MAX_IMAGE_SIZE) {
            double scale = Math.min(
                (double) NEW_WIDTH / width,
                (double) NEW_HEIGHT / height
            );

            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);

            return new BufferedImage(newWidth, newHeight, originalImage.getType());
        } else {
            return originalImage;
        }
    }
}
