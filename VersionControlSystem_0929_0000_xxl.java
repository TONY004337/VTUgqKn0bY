// 代码生成时间: 2025-09-29 00:00:55
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.staticfiles.Location;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VersionControlSystem {

    private static final Map<String, String> repository = new HashMap<>();
    private static final Map<String, Integer> fileVersions = new HashMap<>();
    private static final String REPOSITORY_DIR = "./repository";

    // Initialize Javalin and setup routes
    public void start() {
        Javalin app = Javalin.create()
            .staticFiles.Location(Location.EXTERNAL)
            .start(7000);

        EndpointGroup versionControlRoutes = app.group("/version-control");

        // Add file to the version control system
        versionControlRoutes.post("/add", ctx -> {
            String fileName = ctx.formParam("fileName");
            String fileContent = ctx.formParam("fileContent");
            String commitMessage = ctx.formParam("commitMessage");

            if (fileName == null || fileContent == null || commitMessage == null) {
                ctx.status(400).result("Missing required parameters");
                return;
            }

            commitFile(fileName, fileContent, commitMessage);
            ctx.status(201).result("File added and committed successfully");
        });

        // Commit changes to the file
        versionControlRoutes.post("/commit", ctx -> {
            String fileName = ctx.formParam("fileName");
            String fileContent = ctx.formParam("fileContent");
            String commitMessage = ctx.formParam("commitMessage");

            if (fileName == null || fileContent == null || commitMessage == null) {
                ctx.status(400).result("Missing required parameters");
                return;
            }

            commitFile(fileName, fileContent, commitMessage);
            ctx.status(200).result("Changes committed successfully");
        });

        // Checkout a specific version of the file
        versionControlRoutes.get("/checkout", ctx -> {
            String fileName = ctx.formParam("fileName");
            Integer version = ctx.formParamAsClass("version", Integer.class);

            if (fileName == null || version == null) {
                ctx.status(400).result("Missing required parameters");
                return;
            }

            String fileContent = checkoutFile(fileName, version);
            if (fileContent == null) {
                ctx.status(404).result("File or version not found");
                return;
            }

            ctx.status(200).result("File content: " + fileContent);
        });
    }

    // Commits a file to the version control system
    private void commitFile(String fileName, String fileContent, String commitMessage) {
        String commitId = UUID.randomUUID().toString();
        String filePath = REPOSITORY_DIR + "/" + fileName;

        repository.put(commitId, fileContent);
        fileVersions.put(fileName, fileVersions.getOrDefault(fileName, 0) + 1);

        System.out.println("Committed file: " + filePath + " with commit message: " + commitMessage);
    }

    // Checkouts a specific version of the file
    private String checkoutFile(String fileName, Integer version) {
        int currentVersion = fileVersions.getOrDefault(fileName, 0);
        if (version > currentVersion) {
            System.out.println("Version not found");
            return null;
        }

        // Simulate fetching the file from the repository
        String commitId = getCommitIdByVersion(fileName, version);
        return repository.get(commitId);
    }

    // Helper method to get the commit ID by version
    private String getCommitIdByVersion(String fileName, int version) {
        int count = 0;
        for (Map.Entry<String, String> entry : repository.entrySet()) {
            if (count == version) {
                return entry.getKey();
            }
            count++;
        }
        return null;
    }

    public static void main(String[] args) {
        VersionControlSystem vcs = new VersionControlSystem();
        vcs.start();
    }
}
