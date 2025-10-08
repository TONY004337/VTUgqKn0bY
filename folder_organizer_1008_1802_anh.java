// 代码生成时间: 2025-10-08 18:02:01
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.core.util.Header;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.util.ContextUtil;
import org.eclipse.jetty.http.HttpStatus;

public class FolderOrganizer {

    // Entry point for the application
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.routes(() -> {
            // Define API routes
            ApiBuilder.post("/organize", new Handler<Context>() {
                @Override
                public void handle(Context ctx) throws IOException {
                    organizeFolder(ctx);
                }
            });
        });
    }

    // Method to organize the folder
    private static void organizeFolder(Context ctx) throws IOException {
        // Get the folder path from the request body
        String folderPath = ctx.bodyAsClass(String.class);
        try {
            if (folderPath == null || folderPath.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST_400).result("Folder path cannot be empty.");
                return;
            }

            File folder = new File(folderPath);
            if (!folder.exists() || !folder.isDirectory()) {
                ctx.status(HttpStatus.NOT_FOUND_404).result("Folder not found.");
                return;
            }

            // Organize the folder
            organizeFolderContents(folder);
            ctx.status(HttpStatus.OK_200).result("Folder organized successfully.");
        } catch (Exception e) {
            // Handle any exceptions
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR_500).result("An error occurred: " + e.getMessage());
        }
    }

    // Method to organize the contents of a folder
    private static void organizeFolderContents(File folder) throws IOException {
        // List all files and directories in the folder
        File[] files = folder.listFiles();
        if (files == null) {
            throw new IOException("Unable to list files in the folder.");
        }

        // Separate files and directories
        List<File> directories = new ArrayList<>();
        List<File> filesList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                directories.add(file);
            } else {
                filesList.add(file);
            }
        }

        // Recursively organize directories
        for (File dir : directories) {
            organizeFolderContents(dir);
        }

        // Sort and rename files (if needed)
        filesList.sort((file1, file2) -> file1.getName().compareTo(file2.getName()));
        for (int i = 0; i < filesList.size(); i++) {
            File file = filesList.get(i);
            File newFile = new File(folder, i + "_" + file.getName());
            Files.move(file.toPath(), newFile.toPath());
        }
    }
}
