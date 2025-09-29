// 代码生成时间: 2025-09-29 17:36:43
import io.javalin.Javalin;
# 改进用户体验
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DigitalBankPlatform {

    private static final Map<String, BigDecimal> accounts = new HashMap<>();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.routes(() -> {
            // Route to display account balance
            ApiBuilder.get("/account/:account_id", DigitalBankPlatform::getAccountBalance);
            // Route to deposit money into an account
            ApiBuilder.post("/account/:account_id/deposit", DigitalBankPlatform::depositMoney);
            // Route to withdraw money from an account
# NOTE: 重要实现细节
            ApiBuilder.post("/account/:account_id/withdraw", DigitalBankPlatform::withdrawMoney);
        });

        // Register route overview plugin
        app.registerPlugin(new RouteOverviewPlugin("/"));
    }

    // Handler for getting account balance
    private static void getAccountBalance(Context ctx) {
        String accountId = ctx.pathParam("account_id");
# FIXME: 处理边界情况
        BigDecimal balance = accounts.getOrDefault(accountId, BigDecimal.ZERO);
        ctx.json(balance);
    }

    // Handler for depositing money
    private static void depositMoney(Context ctx) {
# 添加错误处理
        String accountId = ctx.pathParam("account_id");
        String depositAmountStr = ctx.body();
        try {
            BigDecimal depositAmount = new BigDecimal(depositAmountStr);
            if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }
            accounts.merge(accountId, depositAmount, BigDecimal::add);
            ctx.status(201); // Created
        } catch (NumberFormatException e) {
            ctx.status(400); // Bad Request
            ctx.result("Invalid deposit amount format");
        } catch (IllegalArgumentException e) {
            ctx.status(400); // Bad Request
            ctx.result(e.getMessage());
        }
    }

    // Handler for withdrawing money
    private static void withdrawMoney(Context ctx) {
        String accountId = ctx.pathParam("account_id");
        String withdrawAmountStr = ctx.body();
# TODO: 优化性能
        try {
            BigDecimal withdrawAmount = new BigDecimal(withdrawAmountStr);
            if (withdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Withdraw amount must be positive");
            }
            BigDecimal currentBalance = accounts.getOrDefault(accountId, BigDecimal.ZERO);
            if (currentBalance.compareTo(withdrawAmount) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            accounts.merge(accountId, withdrawAmount, (currentBalance, amount) -> currentBalance.subtract(amount));
            ctx.status(200); // OK
# 优化算法效率
        } catch (NumberFormatException e) {
            ctx.status(400); // Bad Request
            ctx.result("Invalid withdraw amount format");
        } catch (IllegalArgumentException e) {
            ctx.status(400); // Bad Request
# 优化算法效率
            ctx.result(e.getMessage());
        }
    }

    // Function to create a new account
    private static String createAccount() {
        String accountId = UUID.randomUUID().toString();
        accounts.put(accountId, BigDecimal.ZERO);
# 添加错误处理
        return accountId;
    }
}
