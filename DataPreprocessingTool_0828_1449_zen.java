// 代码生成时间: 2025-08-28 14:49:39
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;

public class DataPreprocessingTool {

    private static final Javalin app = Javalin.create().start(7000);
    private static final Pattern PATTERN = Pattern.compile("\d+"); // Regular expression pattern to find numbers

    // Entry point of the program
    public static void main(String[] args) {
        app.routes(() -> {
            get("/clean", DataPreprocessingTool::cleanData);
        });
    }

    // Endpoint to clean and preprocess data
    public static String cleanData(String input) {
        try {
            // Remove all non-digit characters and replace with spaces
            String cleanedInput = input.replaceAll("\\D", " ");
            // Trim leading and trailing spaces
            cleanedInput = cleanedInput.trim();
            // Use regular expression to extract numbers and return them
            Matcher matcher = PATTERN.matcher(cleanedInput);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                sb.append(matcher.group()).append(" ");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            // Return error message if any exception occurs
            return "Error: " + e.getMessage();
        }
    }
}
