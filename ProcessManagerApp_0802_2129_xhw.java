// 代码生成时间: 2025-08-02 21:29:14
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessManagerApp {

    private static final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    private static final Method getProcessCpuLoad = getProcessCpuLoadMethod();

    private static Method getProcessCpuLoadMethod() {
        try {
            return OperatingSystemMXBean.class.getMethod("getProcessCpuLoad");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to get method getProcessCpuLoad", e);
        }
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.routes(() -> {
            // Route to get system CPU load
            app.get("/cpu/load", ctx -> {
                double systemCpuLoad = osBean.getSystemCpuLoad();
                ctx.json(String.format({"%s": %.2f"}, "cpuLoad", systemCpuLoad));
            });

            // Route to get process CPU load
            app.get("/process/cpu/load", ctx -> {
                try {
                    double processCpuLoad = (double) getProcessCpuLoad.invoke(osBean);
                    ctx.json(String.format({"%s": %.2f"}, "cpuLoad", processCpuLoad));
                } catch (Exception e) {
                    ctx.status(500).result("Failed to get process CPU load");
                }
            });

            // Route to get process list
            app.get("/processes", ctx -> {
                try {
                    List<Process> processes = Process.list();
                    List<Map<String, Object>> processList = processes.stream()
                        .map(process -> {
                            Map<String, Object> processInfo = new HashMap<>();
                            processInfo.put("pid", process.pid());
                            processInfo.put("name", process.info().command().orElse("Unknown"));
                            return processInfo;
                        })
                        .collect(Collectors.toList());
                    ctx.json(processList);
                } catch (IOException e) {
                    ctx.status(500).result("Failed to get process list");
                }
            });
        });
    }
}
