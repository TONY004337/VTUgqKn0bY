// 代码生成时间: 2025-08-06 03:10:23
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileBackupSyncTool {

    // 备份源文件夹中的文件到目标文件夹
    public static void backupFiles(String sourceDir, String targetDir) {
        try {
            // 确保源目录和目标目录存在
            File sourceDirFile = new File(sourceDir);
            File targetDirFile = new File(targetDir);
            if (!sourceDirFile.exists() || !sourceDirFile.isDirectory()) {
                throw new IllegalArgumentException("源目录不存在或不是目录");
            }
            if (!targetDirFile.exists()) {
                targetDirFile.mkdirs();
            }
            
            // 遍历源目录中的所有文件和子目录
            try (Stream<Path> paths = Files.walk(Paths.get(sourceDir))) {
                paths.forEach(path -> {
                    Path targetPath = Paths.get(targetDir, path.toString().substring(sourceDir.length()));
                    try {
                        Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.err.println("备份文件时发生错误：" + e.getMessage());
                    }
                });
            }
            System.out.println("备份完成");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("备份错误：" + e.getMessage());
        }
    }

    // 同步源文件夹和目标文件夹之间的文件差异
    public static void syncFiles(String sourceDir, String targetDir) {
        try {
            // 确保源目录和目标目录存在
            File sourceDirFile = new File(sourceDir);
            File targetDirFile = new File(targetDir);
            if (!sourceDirFile.exists() || !sourceDirFile.isDirectory()) {
                throw new IllegalArgumentException("源目录不存在或不是目录");
            }
            if (!targetDirFile.exists()) {
                targetDirFile.mkdirs();
            }
            
            // 同步源目录和目标目录之间的文件差异
            try (Stream<Path> sourcePaths = Files.walk(Paths.get(sourceDir));
                 Stream<Path> targetPaths = Files.walk(Paths.get(targetDir))) {
                sourcePaths.forEach(sourcePath -> {
                    Path relativePath = Paths.get(sourceDir).relativize(sourcePath);
                    Path targetPath = Paths.get(targetDir, relativePath.toString());
                    if (!Files.exists(targetPath) || Files.getLastModifiedTime(sourcePath)
                        .compareTo(Files.getLastModifiedTime(targetPath)) > 0) {
                        try {
                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("同步文件：" + relativePath.toString());
                        } catch (IOException e) {
                            System.err.println("同步文件时发生错误：" + e.getMessage());
                        }
                    }
                });
                targetPaths.forEach(targetPath -> {
                    Path relativePath = Paths.get(targetDir).relativize(targetPath);
                    Path sourcePath = Paths.get(sourceDir, relativePath.toString());
                    if (!Files.exists(sourcePath)) {
                        try {
                            Files.delete(targetPath);
                            System.out.println("删除文件：" + relativePath.toString());
                        } catch (IOException e) {
                            System.err.println("删除文件时发生错误：" + e.getMessage());
                        }
                    }
                });
            }
            System.out.println("同步完成");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("同步错误：" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // 示例：备份和同步文件夹
        String sourceDir = "/path/to/source";
        String targetDir = "/path/to/target";
        backupFiles(sourceDir, targetDir);
        syncFiles(sourceDir, targetDir);
    }
}