package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class CategoryWithSpendTest {
    private static final Config CFG = Config.getInstance();

    @User(username = "duck",
            categories = {@Category(name = "Test WOW 1", archived = true)},
            spendings = {@Spending(description = "Test spending wow 1", amount = 1234.5, category = "Обучение WOWWWW 1")}
    )
    @Test
    public void checkSpendingAndThatCategoryIsArchived(CategoryJson category, SpendJson spendJson) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(spendJson.username(), "12345")
                .submit()
                .checkSpending(category.name(), String.valueOf(spendJson.amount()), spendJson.description())
                .goToProfile("Profile")
                .clickOnArchivedToggle()
                .checkCategoryStatus(category.name());
    }
}
