// 代码生成时间: 2025-08-12 12:51:21
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.plugin.json.JavalinJson;
import io.javalin.scheduler.Job;
import io.javalin.scheduler.Scheduler;
import java.util.concurrent.TimeUnit;

public class ScheduledTask {

    private static final int SCHEDULED_TASK_INTERVAL = 5; // 5秒定时任务执行间隔

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        JavalinJson.configure(app, "application/json");

        // 定时任务调度器
        Scheduler scheduler = app.getScheduler();

        // 定义定时任务
        EndpointGroup scheduledEndpoints = app.group("/scheduled");
        scheduledEndpoints.get(() -> executorTask("Scheduled task executed."));

        // 启动定时任务
        scheduler.repeat(() -> {
            executorTask("Scheduled task executed.");
        }, SCHEDULED_TASK_INTERVAL, TimeUnit.SECONDS);

        // 定义任务执行方法
        private void executorTask(String message) {
            System.out.println(message);
            // 任务的具体执行逻辑
        }
    }
}
