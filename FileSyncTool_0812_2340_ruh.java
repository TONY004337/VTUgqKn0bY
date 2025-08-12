// 代码生成时间: 2025-08-12 23:40:51
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileSyncTool {

    private static final String SOURCE_FOLDER = "/path/to/source/folder";
    private static final String DESTINATION_FOLDER = "/path/to/destination/folder";

    public static void main(String[] args) {
        try {
            // Ensure the destination folder exists
            Files.createDirectories(Paths.get(DESTINATION_FOLDER));

            // Copy all files from source to destination
            syncFolders(new File(SOURCE_FOLDER), new File(DESTINATION_FOLDER));

            System.out.println("File synchronization completed successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred during file synchronization: " + e.getMessage());
        }
    }

    /**
     * Synchronizes the content of two directories by copying files from source to destination.
     * 
     * @param sourceDir The source directory to copy files from.
     * @param destinationDir The destination directory to copy files to.
     * @throws IOException If an I/O error occurs during file copying.
     */
    public static void syncFolders(File sourceDir, File destinationDir) throws IOException {
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Source directory does not exist or is not a directory.");
        }

        if (!destinationDir.exists() || !destinationDir.isDirectory()) {
            throw new IllegalArgumentException("Destination directory does not exist or is not a directory.");
        }

        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                File destinationFile = new File(destinationDir, file.getName());

                if (file.isDirectory()) {
                    // Recursively sync subdirectories
                    syncFolders(file, destinationFile);
                } else {
                    // Copy file from source to destination
                    copyFile(file, destinationFile);
                }
            }
        }
    }

    /**
     * Copies a file from source to destination.
     * 
     * @param sourceFile The source file to copy.
     * @param destinationFile The destination file to copy to.
     * @throws IOException If an I/O error occurs during file copying.
     */
    public static void copyFile(File sourceFile, File destinationFile) throws IOException {
        if (!destinationFile.exists() || destinationFile.lastModified() < sourceFile.lastModified()) {
            try (InputStream in = new FileInputStream(sourceFile);
                 OutputStream out = new FileOutputStream(destinationFile)) {
                // Copy the contents of the input stream to the output stream
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } catch (IOException e) {
                throw new IOException("Error copying file from " + sourceFile.getAbsolutePath() + " to " + destinationFile.getAbsolutePath(), e);
            }
        } else {
            System.out.println("File is up-to-date: " + destinationFile.getAbsolutePath());
        }
    }
}
