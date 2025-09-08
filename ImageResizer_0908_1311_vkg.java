// 代码生成时间: 2025-09-08 13:11:02
import com.javalin.Javalin;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
# TODO: 优化性能
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
# 扩展功能模块
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
# NOTE: 重要实现细节
import java.util.List;
import java.util.stream.Stream;

public class ImageResizer {

    private static final String IMAGES_DIR = "path/to/images";
    private static final String DEST_DIR = "path/to/destination";
    private static final String[] ALLOWED_FORMATS = { "jpg", "jpeg", "png", "gif" };
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 800;

    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);

        app.post("/batch-resize", ctx -> {
            String sourceDir = ctx.formParam("sourceDir");
            String destDir = ctx.formParam("destDir");
            String[] formats = ctx.formParam("formats").split(",");

            try {
                resizeImages(new File(sourceDir), new File(destDir), formats);
                ctx.status(200).result("Images resized successfully.");
            } catch (IOException e) {
                ctx.status(500).result("Error resizing images: " + e.getMessage());
            }
        });

        app.get("/health", ctx -> ctx.result("Server is up and running."));
    }

    private static void resizeImages(File sourceDir, File destDir, String[] formats) throws IOException {
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IOException("Source directory does not exist or is not a directory.");
# 扩展功能模块
        }

        if (!destDir.exists() && !destDir.mkdirs()) {
            throw new IOException("Failed to create destination directory.");
        }
# 改进用户体验

        try (Stream<File> files = Files.walk(Paths.get(sourceDir.getAbsolutePath()))) {
            files.filter(file -> isAllowedFormat(file, formats))
                 .forEach(file -> {
                     try {
                         resizeImage(file, destDir, new File(destDir, FilenameUtils.getBaseName(file.getName()) + "_resized." +
                                 FilenameUtils.getExtension(file.getName())))
                             .forEach(imaging -> {
                                 try (OutputStream out = new FileOutputStream(new File(destDir, "resized_" + file.getName()))) {
                                     Imaging.writeImage(imaging, out);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             });
                     } catch (IOException | ImagingOpException e) {
                         e.printStackTrace();
# 改进用户体验
                     }
                 });
        }
    }

    private static boolean isAllowedFormat(File file, String[] formats) {
        return formats.length == 0 || Stream.of(formats).anyMatch(format -> FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(format));
    }

    private static List<BufferedImage> resizeImage(File file, File destDir, File destFile) throws IOException, ImagingOpException {
        List<BufferedImage> resizedImages = new ArrayList<>();
        InputStream inputStream = new FileInputStream(file);
        BufferedImage originalImage = Imaging.getBufferedImage(inputStream);

        if (originalImage.getWidth() > MAX_WIDTH || originalImage.getHeight() > MAX_HEIGHT) {
            BufferedImage resizedImage = new BufferedImage(MAX_WIDTH, MAX_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImage.createGraphics();

            ImageObserver imageObserver = new ImageObserver() {
# TODO: 优化性能
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return true;
                }
            };
# 增强安全性
            g2d.drawImage(originalImage, 0, 0, MAX_WIDTH, MAX_HEIGHT, imageObserver);
            g2d.dispose();

            resizedImages.add(resizedImage);
        } else {
            resizedImages.add(originalImage);
# 扩展功能模块
        }

        return resizedImages;
# NOTE: 重要实现细节
    }
}
