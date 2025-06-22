package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Успешная авторизация")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("duck", "12345")
                .submit()
                .checkThatPageLoaded();
    }

    @Test
    @DisplayName("Попыта авторизации с не корректными значениями логина и пароля")
    void mainPageShouldNotBeDisplayedAfterFailureLogin() {
        LoginPage loginPage = new LoginPage();
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("duck333333", "1p")
                .submit();
        loginPage
                .checkErrorMessageBlock("Неверные учетные данные пользователя");

    }
}
