// 代码生成时间: 2025-09-07 14:52:04
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 一个简单的批量文件重命名工具
public class BatchFileRenamer {

    // 重命名文件的前缀
    private static final String PREFIX = "new_";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入文件夹路径：");
        String directoryPath = scanner.nextLine();

        // 检查路径是否存在
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("指定的路径不存在或者不是一个文件夹");
            return;
        }

        System.out.println("请输入要重命名的文件数量：");
        int count = scanner.nextInt();
        scanner.nextLine(); // 清除换行符

        // 读取文件列表
        File[] files = directory.listFiles();
        if (files == null || files.length < count) {
            System.out.println("文件数量不足");
            return;
        }

        // 重命名文件
        for (int i = 0; i < count; i++) {
            File file = files[i];
            if (file.isFile()) {
                try {
                    Path path = file.toPath();
                    String newName = PREFIX + (i + 1) + "." + Files.probeContentType(path).split("/").pop();
                    Files.move(path, path.resolveSibling(newName));
                    System.out.println("文件重命名成功: " + newName);
                } catch (IOException e) {
                    System.err.println("文件重命名失败: " + file.getName());
                }
            }
        }
    }
}
