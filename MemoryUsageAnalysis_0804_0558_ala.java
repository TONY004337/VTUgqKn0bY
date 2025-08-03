// 代码生成时间: 2025-08-04 05:58:49
 * This Javalin application provides a simple endpoint to analyze memory usage on the server.
 * It uses the java.lang.management package to fetch memory usage statistics.
 */

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import com.fasterxml.jackson.databind.ObjectMapper;
import static java.lang.management.ManagementFactory.MEMORY_MXBEAN_NAME;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class MemoryUsageAnalysis {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000); // Set the port to 7000

        app.get("/memory", MemoryUsageAnalysis::handleMemoryUsageRequest);
    }

    private static void handleMemoryUsageRequest(Context ctx) {
        try {
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName(MEMORY_MXBEAN_NAME);
            MemoryUsage heapMemoryUsage = (MemoryUsage) platformMBeanServer.getAttribute(objectName, "HeapMemoryUsage");
            MemoryUsage nonHeapMemoryUsage = (MemoryUsage) platformMBeanServer.getAttribute(objectName, "NonHeapMemoryUsage");

            // Prepare the response as JSON
            MemoryUsageData data = new MemoryUsageData(heapMemoryUsage, nonHeapMemoryUsage);
            ctx.result(objectMapper.writeValueAsString(data));
            ctx.status(200); // OK
        } catch (Exception e) {
            ctx.result("Error while retrieving memory usage: " + e.getMessage());
            ctx.status(500); // Internal Server Error
        }
    }

    /*
     * Data class to hold memory usage statistics.
     */
    static class MemoryUsageData {
        private long heapInit;
        private long heapUsed;
        private long heapCommitted;
        private long heapMax;
        private long heapUsedPercent;
        private long nonHeapInit;
        private long nonHeapUsed;
        private long nonHeapCommitted;
        private long nonHeapMax;
        private long nonHeapUsedPercent;

        public MemoryUsageData(MemoryUsage heapMemoryUsage, MemoryUsage nonHeapMemoryUsage) {
            this.heapInit = heapMemoryUsage.getInit();
            this.heapUsed = heapMemoryUsage.getUsed();
            this.heapCommitted = heapMemoryUsage.getCommitted();
            this.heapMax = heapMemoryUsage.getMax();
            this.heapUsedPercent = calculatePercentage(heapMemoryUsage.getUsed(), heapMemoryUsage.getMax());

            this.nonHeapInit = nonHeapMemoryUsage.getInit();
            this.nonHeapUsed = nonHeapMemoryUsage.getUsed();
            this.nonHeapCommitted = nonHeapMemoryUsage.getCommitted();
            this.nonHeapMax = nonHeapMemoryUsage.getMax();
            this.nonHeapUsedPercent = calculatePercentage(nonHeapMemoryUsage.getUsed(), nonHeapMemoryUsage.getMax());
        }

        // Getters and setters for the fields
        public long getHeapInit() { return heapInit; }
        public void setHeapInit(long heapInit) { this.heapInit = heapInit; }
        public long getHeapUsed() { return heapUsed; }
        public void setHeapUsed(long heapUsed) { this.heapUsed = heapUsed; }
        public long getHeapCommitted() { return heapCommitted; }
        public void setHeapCommitted(long heapCommitted) { this.heapCommitted = heapCommitted; }
        public long getHeapMax() { return heapMax; }
        public void setHeapMax(long heapMax) { this.heapMax = heapMax; }
        public long getHeapUsedPercent() { return heapUsedPercent; }
        public void setHeapUsedPercent(long heapUsedPercent) { this.heapUsedPercent = heapUsedPercent; }
        public long getNonHeapInit() { return nonHeapInit; }
        public void setNonHeapInit(long nonHeapInit) { this.nonHeapInit = nonHeapInit; }
        public long getNonHeapUsed() { return nonHeapUsed; }
        public void setNonHeapUsed(long nonHeapUsed) { this.nonHeapUsed = nonHeapUsed; }
        public long getNonHeapCommitted() { return nonHeapCommitted; }
        public void setNonHeapCommitted(long nonHeapCommitted) { this.nonHeapCommitted = nonHeapCommitted; }
        public long getNonHeapMax() { return nonHeapMax; }
        public void setNonHeapMax(long nonHeapMax) { this.nonHeapMax = nonHeapMax; }
        public long getNonHeapUsedPercent() { return nonHeapUsedPercent; }
        public void setNonHeapUsedPercent(long nonHeapUsedPercent) { this.nonHeapUsedPercent = nonHeapUsedPercent; }
    }

    /*
     * Helper method to calculate the percentage usage of memory.
     */
    private static long calculatePercentage(long used, long max) {
        if (max == 0) return 0;
        return (long) (100.0 * ((double) used / (double) max));
    }
}
