// 代码生成时间: 2025-10-03 15:25:47
// FileMergeService.java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import io.javalin.http.Handler;

// 一个简单的文件分割合并工具，使用JAVA和JAVALIN框架实现
public class FileMergeService {

    private static final String TEMP_DIRECTORY = "temp/";

    // 分割文件
    public void splitFile(String filePath, int chunkSize) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File does not exist or is a directory");
        }

        int numChunks = (int) Math.ceil((double) file.length() / chunkSize);
        for (int i = 0; i < numChunks; i++) {
            String chunkFileName = TEMP_DIRECTORY + file.getName() + "_chunk_" + i;
            try (FileInputStream fis = new FileInputStream(file);
                 FileChannel fcin = fis.getChannel();
                 FileOutputStream fos = new FileOutputStream(chunkFileName);
                 FileChannel fcout = fos.getChannel()) {

                long size = (i < numChunks - 1) ? chunkSize : (file.length() - i * chunkSize);
                fcout.transferFrom(fcin, i * chunkSize, size);
            } catch (IOException e) {
                throw new IOException("Failed to split file", e);
            }
        }
    }

    // 合并文件
    public void mergeFiles(String outputFilePath, String... chunkFiles) throws IOException {
        if (!Arrays.stream(chunkFiles).allMatch(File::exists)) {
            throw new IllegalArgumentException("Not all chunks exist");
        }

        try (FileOutputStream fos = new FileOutputStream(outputFilePath);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            Arrays.stream(chunkFiles).forEach(chunkFile -> {
                try (FileInputStream fis = new FileInputStream(chunkFile);
                     BufferedInputStream bis = new BufferedInputStream(fis)) {

                    bis.transferTo(bos);
                } catch (IOException e) {
                    throw new IOException("Failed to merge file", e);
                }
            });
        } catch (IOException e) {
            throw new IOException("Failed to merge files", e);
        }
    }

    // 创建Javalin路由处理程序
    public Handler createRouteHandler() {
        return ctx -> {
            try {
                String filePath = ctx.queryParam("filePath");
                int chunkSize = Integer.parseInt(ctx.queryParam("chunkSize"));

                // 调用分割文件方法
                splitFile(filePath, chunkSize);

                ctx.result("File split successfully");
            } catch (Exception e) {
                ctx.status(400);
                ctx.result("Error: " + e.getMessage());
            }
        };
    }

    // 清理临时目录
    public void cleanTempDirectory() {
        try (Stream<Path> paths = Files.walk(Paths.get(TEMP_DIRECTORY))) {
            paths
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
