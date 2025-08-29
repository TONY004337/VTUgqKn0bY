// 代码生成时间: 2025-08-30 05:12:32
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class FileDecompressor {

    // Javalin 服务对象
    private Javalin app;
    
    public FileDecompressor() {
        // 初始化 Javalin 服务
        app = Javalin.create().start(7000);
        
        // 设置路由
        app.post("/decompress", this::decompressFile);
    }

    // 解压文件的方法
    private void decompressFile(Context ctx) {
        try {
            // 获取上传的文件
            File uploadedFile = ctx.uploadFile("file").getFile();
            
            // 检查文件是否为空
            if (uploadedFile == null || uploadedFile.length() == 0) {
                ctx.status(400).result("No file uploaded.");
                return;
            }
            
            // 创建解压目录
            String outputFolder = "./output" + File.separator + uploadedFile.getName() + "_decompressed";
            new File(outputFolder).mkdirs();
            
            // 读取文件输入流
            FileInputStream fis = new FileInputStream(uploadedFile);
            ZipInputStream zipIn = new ZipInputStream(fis);
            ZipEntry zipEntry = zipIn.getNextEntry();
            // 循环遍历zip内的条目
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                
                // 创建文件
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    newFile.createNewFile();
                    // 复制文件内容
                    FileOutputStream fos = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipIn.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipIn.closeEntry();
                zipEntry = zipIn.getNextEntry();
            }
            zipIn.close();
            fis.close();
            
            // 解压成功，返回解压后的目录路径
            ctx.status(200).result("File decompressed successfully. Output folder: " + outputFolder);
        } catch (IOException e) {
            // 错误处理
            ctx.status(500).result("Error occurred while decompressing file: " + e.getMessage());
        }
    }
    
    // 主方法，启动服务
    public static void main(String[] args) {
        // 创建压缩文件解压工具实例
        FileDecompressor decompressor = new FileDecompressor();
        
        // 保持服务运行
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
