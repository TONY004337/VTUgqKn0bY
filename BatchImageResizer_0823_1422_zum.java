// 代码生成时间: 2025-08-23 14:22:53
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.util.Header;
import io.javalin.http.util.MultipartUtil;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchImageResizer {

    private static final Logger logger = LoggerFactory.getLogger(BatchImageResizer.class);
    private static final String UPLOAD_DIRECTORY = "uploads/";
    private static final int MAX_DIMENSION = 1024; // Maximum dimension in pixels

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.post("/resize", BatchImageResizer::handleResize);
    }

    private static void handleResize(Context ctx) {
        try {
            // Get the uploaded files
            List<MultipartFile> files = ctx.multipartFiles("files");
            if (files.isEmpty()) {
                ctx.status(400).result("No files to resize");
                return;
            }

            // Resize the images
            for (MultipartFile file : files) {
                resizeImage(file);
            }

            ctx.status(200).result("Images resized successfully");
        } catch (IOException e) {
            logger.error("Error resizing images", e);
            ctx.status(500).result("Error resizing images");
        }
    }

    private static void resizeImage(MultipartFile file) throws IOException {
        // Ensure the upload directory exists
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Read the uploaded image
        BufferedImage image = ImageIO.read(file.getInputStream());

        // Calculate the new dimensions
        double ratio = Math.min((double) MAX_DIMENSION / image.getWidth(),
                             (double) MAX_DIMENSION / image.getHeight());
        int newWidth = (int) (image.getWidth() * ratio);
        int newHeight = (int) (image.getHeight() * ratio);

        // Resize the image
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(
            Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, newWidth), 0, 0, null
        );

        // Write the resized image to the upload directory
        String extension = FilenameUtils.getExtension(file.getFileName());
        File outputFile = new File(UPLOAD_DIRECTORY + file.getFileName().replace("." + extension, "_resized." + extension));
        ImageIO.write(resizedImage, extension, outputFile);
    }

    public static class MultipartFile {
        private final String fileName;
        private final Long fileSize;
        private final String mimeType;
        private final InputStream inputStream;

        public MultipartFile(String fileName, Long fileSize, String mimeType, InputStream inputStream) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.mimeType = mimeType;
            this.inputStream = inputStream;
        }

        public String getFileName() {
            return fileName;
        }

        public Long getFileSize() {
            return fileSize;
        }

        public String getMimeType() {
            return mimeType;
        }

        public InputStream getInputStream() {
            return inputStream;
        }
    }
}
