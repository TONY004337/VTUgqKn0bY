// 代码生成时间: 2025-08-14 21:43:08
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

// FolderOrganizer is a class that organizes the folder structure by moving files into
// subfolders based on certain criteria, such as file extensions.
public class FolderOrganizer {

    // The path to the directory to be organized.
    private Path directoryPath;

    // Constructor that takes the directory path as an argument.
    public FolderOrganizer(String directoryPath) {
        this.directoryPath = Paths.get(directoryPath);
    }

    // Organizes the files in the specified directory into subfolders by their file extensions.
    public void organizeByExtension() throws IOException {
        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }

        // Get all files in the directory.
        File[] files = directoryPath.toFile().listFiles();

        if (files == null) {
            return; // The directory is empty or inaccessible.
        }

        // Iterate through each file and move it to a corresponding subfolder.
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf('.');
                String extension = dotIndex == -1 ? "Others" : fileName.substring(dotIndex + 1);
                Path targetFolder = directoryPath.resolve("." + extension);

                // Create the subfolder if it does not exist.
                Files.createDirectories(targetFolder);

                // Move the file to the subfolder.
                Files.move(file.toPath(), targetFolder.resolve(fileName));
            }
        }
    }

    // Main method to run the FolderOrganizer.
    public static void main(String[] args) {
        try {
            // Use a valid path to the directory you want to organize.
            FolderOrganizer organizer = new FolderOrganizer("path/to/your/directory");
            organizer.organizeByExtension();
            System.out.println("Folder organizing completed.");
        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
