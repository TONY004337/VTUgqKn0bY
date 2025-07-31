// 代码生成时间: 2025-08-01 06:09:14
import io.javalin.Javalin;
import io.javalin.http.Handler;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

public class SystemPerformanceMonitor {

    // Main method to run the Javalin server
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.get("/performance", new Handler() {
            @Override
            public void handle(Context ctx) {
                try {
                    Map<String, Object> metrics = getSystemMetrics();
                    ctx.json(metrics);
                } catch (Exception e) {
                    ctx.status(500);
                    ctx.result("Internal Server Error: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Method to get system metrics
     * 
     * @return Map containing system metrics
     */
    private static Map<String, Object> getSystemMetrics() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        Map<String, Object> metrics = new HashMap<>();

        metrics.put("cpuLoad", osBean.getSystemCpuLoad());
        metrics.put("freeMemory", runtimeBean.getFreeMemory());
        metrics.put("totalMemory", runtimeBean.getTotalMemory());
        metrics.put("maxMemory", runtimeBean.getMaxMemory());
        metrics.put("usedMemory", runtimeBean.getTotalMemory() - runtimeBean.getFreeMemory());

        return metrics;
    }
}
