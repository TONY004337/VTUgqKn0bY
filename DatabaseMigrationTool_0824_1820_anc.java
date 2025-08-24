// 代码生成时间: 2025-08-24 18:20:55
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseMigrationTool {

    private static final String DATABASE_URL = "jdbc:your_database_url";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";
    private static final String MIGRATION_LOCATION = "classpath:db/migration";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        registerEndpoints(app);
    }

    private static void registerEndpoints(Javalin app) {
        app.get("/migrate", ctx -> migrateDatabase(ctx));
        app.get("/status", ctx -> databaseMigrationStatus(ctx));
    }

    private static void migrateDatabase(Context ctx) {
        try {
            ClassicConfiguration config = new ClassicConfiguration();
            config.setDataSource(DATABASE_URL, USER, PASSWORD);
            config.setLocations(MIGRATION_LOCATION);
            Flyway flyway = new Flyway(config);
            flyway.migrate();
            ctx.status(200).result("Migration successful");
        } catch (Exception e) {
            ctx.status(500).result("Migration failed: " + e.getMessage());
        }
    }

    private static void databaseMigrationStatus(Context ctx) {
        try {
            ClassicConfiguration config = new ClassicConfiguration();
            config.setDataSource(DATABASE_URL, USER, PASSWORD);
            config.setLocations(MIGRATION_LOCATION);
            Flyway flyway = new Flyway(config);
            List<MigrationInfo> migrations = flyway.info().all();
            List<String> pendingMigrations = migrations.stream()
                    .filter(migrationInfo -> migrationInfo.getState() == MigrationState.PENDING)
                    .map(migrationInfo -> migrationInfo.getVersion().toString())
                    .collect(Collectors.toList());
            JSONObject status = new JSONObject();
            status.put("total", migrations.size());
            status.put("pending", pendingMigrations);
            ctx.status(200).result(status.toString());
        } catch (SQLException e) {
            ctx.status(500).result("Error retrieving migration status: " + e.getMessage());
        }
    }
}
