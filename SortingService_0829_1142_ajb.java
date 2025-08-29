// 代码生成时间: 2025-08-29 11:42:32
package com.example;

import io.javalin.ApiBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SortingService {

    // 功能：对整数列表进行排序
    // 输入：int[] numbers - 要排序的整数数组
    // 输出：String - 排序后的整数列表，以逗号分隔
    public String sortNumbers(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }

        return Arrays.stream(numbers)
                .sorted()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }

    // Javalin 路由设置方法
    public static void setupRoutes() {
        io.javalin.Javalin app = io.javalin.Javalin.create().start(7000);
        app.routes(() -> {
            app.get("/sort", ctx -> {
                String numbersStr = ctx.queryParam("numbers");
                if (numbersStr == null || numbersStr.isEmpty()) {
                    ctx.status(400);
                    ctx.result("Query parameter 'numbers' is required");
                } else {
                    try {
                        String[] numbersArr = numbersStr.split(",");
                        int[] numbers = Arrays.stream(numbersArr)
                                .mapToInt(Integer::parseInt)
                                .toArray();
                        SortingService service = new SortingService();
                        String sortedNumbers = service.sortNumbers(numbers);
                        ctx.result(sortedNumbers);
                    } catch (Exception e) {
                        ctx.status(400);
                        ctx.result("Invalid input format");
                    }
                }
            });
        });
    }

    // 主方法，用于程序启动
    public static void main(String[] args) {
        setupRoutes();
    }
}
