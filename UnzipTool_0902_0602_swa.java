// 代码生成时间: 2025-09-02 06:02:24
import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnzipTool {

    // Entry point of the application
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.post("/unzip", UnzipTool::handleUnzip);
    }

    // Handler method to handle unzip requests
    private static void handleUnzip(Context ctx) throws IOException {
        List<String> filePaths = ctx.formParamValues("file")
            .stream()
            .flatMap(path -> listFilesRecursively(path).stream())
            .collect(Collectors.toList());

        try {
            filePaths.forEach(filePath -> {
                try {
                    unzipFile(filePath);
                } catch (IOException e) {
                    System.err.println("Error unzipping file: " + filePath);
                    e.printStackTrace();
                }
            });

            ctx.status(200).result("Files successfully unzipped");
        } catch (IOException e) {
            ctx.status(500).result("Error processing unzip request");
       }
    }

    // Method to recursively list files in a directory
    private static Stream<String> listFilesRecursively(String dir) {
        Path startPath = Paths.get(dir);
        try (Stream<Path> stream = Files.walk(startPath)) {
            return stream
                .filter(Files::isRegularFile)
                .map(Path::toString);
        } catch (IOException e) {
            System.err.println("Error listing files in directory: " + dir);
            e.printStackTrace();
            return Stream.empty();
        }
    }

    // Method to unzip a single file
    private static void unzipFile(String filePath) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(filePath));
        ZipEntry entry = zipIn.getNextEntry();

        // Process the zip file entries
        while (entry != null) {
            String outPath = Paths.get(filePath).getParent().resolve(entry.getName()).toString();
            extractFile(zipIn, outPath);
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    // Method to extract a file from the zip input stream
    private static void extractFile(ZipInputStream zipIn, String outPath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
