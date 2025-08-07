// 代码生成时间: 2025-08-08 02:51:07
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.staticfiles.Location;

public class FolderOrganizer {

    // 定义根目录
    private static final String ROOT_DIRECTORY = "./sorted_folders";

    public static void main(String[] args) {
        Javalin app = Javalin.create()
            .staticFiles.Location(Location.EXTERNAL)
            .start(7000);

        app.post("/sort", new Handler<Context>() {
            @Override
            public void handle(Context ctx) {
                try {
                    String directoryPath = ctx.bodyAsClass(String.class);
                    sortDirectory(new File(directoryPath));
                    ctx.result("Directory sorted successfully.");
                } catch (Exception e) {
                    ctx.status(500);
                    ctx.result("Error sorting directory: " + e.getMessage());
                }
            }
        });
    }

    private static void sortDirectory(File directory) {
        // 检查目录是否存在且是否为目录
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a valid directory.");
        }

        // 获取目录中的所有文件及子目录
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IllegalStateException("Unable to list files in the directory.");
        }

        // 按文件名排序
        Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));

        // 创建子目录并递归排序
        for (File file : files) {
            if (file.isDirectory()) {
                // 确保子目录结构存在
                new File(ROOT_DIRECTORY + "/" + file.getName()).mkdirs();
                sortDirectory(file);
            }
        }
    }
}
