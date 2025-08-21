// 代码生成时间: 2025-08-21 20:07:23
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.core.util.Slurp;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemPerformanceMonitor {

    private static final Pattern CPU_REGEX = Pattern.compile("(\d+\.\d+)%", Pattern.CASE_INSENSITIVE);
    private static final OperatingSystemMXBean OS_MX_BEAN = ManagementFactory.getOperatingSystemMXBean();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        initializeEndpoints(app);
    }

    private static void initializeEndpoints(Javalin app) {
        app.routes(() -> {
            ApiBuilder.get("/monitor", SystemPerformanceMonitor::getSystemPerformance);
        });
    }

    private static void getSystemPerformance(Context ctx) {
        try {
            double cpuLoad = OS_MX_BEAN.getSystemCpuLoad();
            double freeMemory = Runtime.getRuntime().freeMemory() / (1024.0 * 1024);
            double totalMemory = Runtime.getRuntime().totalMemory() / (1024.0 * 1024);
            double usedMemory = (totalMemory - freeMemory);
            long uptime = OS_MX_BEAN.getSystemUptime();

            Map<String, Object> performanceData = new HashMap<>();
            performanceData.put("cpuLoad", cpuLoad);
            performanceData.put("freeMemoryMB", freeMemory);
            performanceData.put("usedMemoryMB", usedMemory);
            performanceData.put("totalMemoryMB", totalMemory);
            performanceData.put("uptimeSeconds", uptime);

            ctx.json(performanceData);
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error: " + Slurp.string(e));
        }
    }
}
