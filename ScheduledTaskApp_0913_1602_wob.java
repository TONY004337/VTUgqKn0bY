// 代码生成时间: 2025-09-13 16:02:30
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskApp {

    private static final int PORT = 7000; // Define the port number
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(PORT);

        // Define the endpoint for scheduling tasks
        app.post("/schedule", ctx -> {
            String taskName = ctx.bodyAsClass(Task.class).taskName;
            String cronExpression = ctx.bodyAsClass(Task.class).cronExpression;
            try {
                scheduleTask(taskName, cronExpression);
                ctx.status(200).result("Task scheduled successfully");
            } catch (Exception e) {
                ctx.status(400).result("Error scheduling task: " + e.getMessage());
            }
        });

        app.get("/", ctx -> ctx.result("Scheduled Task Scheduler is running"));
    }

    /**
     * Schedules a task using a cron expression
     *
     * @param taskName The name of the task
     * @param cronExpression The cron expression for scheduling the task
     */
    public static void scheduleTask(String taskName, String cronExpression) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Executing task: " + taskName);
                // Here you would add the logic for your task
            }
        };
        long delay = 0;
        int period = 1; // The period is set to 1 second for demo purposes
        timer.scheduleAtFixedRate(task, delay, period);

        // For demonstration, we'll use a scheduled executor service to execute tasks based on cron expressions
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Executing scheduled task: " + taskName);
            // Here you would add the logic for your scheduled task
        }, 0, Long.parseLong(cronExpression.split(" \")[1]), TimeUnit.SECONDS);
    }

    /**
     * Represents a task to be scheduled
     */
    public static class Task {
        private String taskName;
        private String cronExpression;

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getCronExpression() {
            return cronExpression;
        }

        public void setCronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
        }
    }
}
