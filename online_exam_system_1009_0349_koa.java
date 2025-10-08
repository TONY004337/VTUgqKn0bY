// 代码生成时间: 2025-10-09 03:49:24
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class OnlineExamSystem {

    // Main method to start the Javalin server
    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);
        app.routes(() -> {
            // Endpoint to get all questions
            get("/questions", OnlineExamSystem::getAllQuestions);
            // Endpoint to get a question by id
            get("/questions/:id", OnlineExamSystem::getQuestionById);
            // Endpoint to submit answers
            post("/submit", OnlineExamSystem::submitAnswers);
        });
    }

    // Method to handle GET request for all questions
    public static void getAllQuestions(Context ctx) {
        List<Map<String, Object>> questions = new ArrayList<>();
        // Simulated database of questions
        questions.add(createQuestion("What is the capital of France?", "Paris"));
        questions.add(createQuestion("What is 2 + 2?", "4"));
        // Return questions as JSON
        ctx.json(questions);
    }

    // Method to handle GET request for a single question by id
    public static void getQuestionById(Context ctx) {
        String id = ctx.pathParam("id");
        // Simulated database lookup
        Map<String, Object> question = createQuestion("What is the capital of France?", "Paris");
        if ("1".equals(id)) {
            ctx.json(question);
        } else {
            ctx.status(404); // Not Found
            ctx.json("Question not found");
        }
    }

    // Method to handle POST request to submit answers
    public static void submitAnswers(Context ctx) {
        Map<String, String> answers = ctx.bodyAsClass(Map.class);
        // Simulate checking answers
        boolean isCorrect = checkAnswers(answers);
        if (isCorrect) {
            ctx.status(200);
            ctx.json("All answers are correct");
        } else {
            ctx.status(400); // Bad Request
            ctx.json("Some answers are incorrect");
        }
    }

    // Helper method to create a question
    private static Map<String, Object> createQuestion(String question, String correctAnswer) {
        Map<String, Object> questionMap = new HashMap<>();
        questionMap.put("question", question);
        questionMap.put("correctAnswer", correctAnswer);
        return questionMap;
    }

    // Helper method to simulate checking answers
    private static boolean checkAnswers(Map<String, String> answers) {
        // Here you would have logic to check the answers against the correct ones
        // For simplicity, let's assume all submitted answers are correct
        return true;
    }
}
