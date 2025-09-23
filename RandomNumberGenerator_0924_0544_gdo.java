// 代码生成时间: 2025-09-24 05:44:54
 * It includes basic error handling and documentation for clarity and maintainability.
 */

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumberGenerator {
    private static final int DEFAULT_MIN = 1;
    private static final int DEFAULT_MAX = 100;
    private static final String PATH = "/random";
    private static final String PARAM_MIN = "min";
    private static final String PARAM_MAX = "max";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.routes(() -> {
            // GET /random - generates a random number between two ranges
            get(PATH, ctx -> {
                try {
                    int min = getRange(ctx, PARAM_MIN, DEFAULT_MIN);
                    int max = getRange(ctx, PARAM_MAX, DEFAULT_MAX);

                    if (min >= max) {
                        ctx.status(400);
                        ctx.result("Minimum value must be less than maximum value.");
                        return;
                    }

                    int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
                    ctx.json(