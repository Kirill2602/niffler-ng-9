package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "duck",
            spendings = {@Spending(
                    amount = 89990.00,
                    description = "Advanced 9 поток!",
                    category = "Обучение"
            )}
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(SpendJson spendJson) {
        final String newDescription = ":)";

        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(spendJson.username(), "12345")
                .submit()
                .checkThatPageLoaded()
                .editSpending(spendJson.description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContainsSpending(newDescription);
    }
}
