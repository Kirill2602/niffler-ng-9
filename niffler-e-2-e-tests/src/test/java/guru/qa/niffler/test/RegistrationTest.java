package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomUsername;

public class RegistrationTest {
    private final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Успешная регистрация")
    public void modalWindowShodBeVisibleAfterSuccessRegistration() {

        open(CFG.frontUrl(), LoginPage.class)
                .clickOnCreateAccountButton()
                .checkHeaderText("Sign up")
                .fillingRegistrationPage(getRandomUsername(), "12345", "12345")
                .checkSuccessRegistrationMessageAndGoToSignInPage()
                .checkLoginHeader("Log in");
    }

    @DisabledByIssue(value = "2")
    @Test
    @DisplayName("Попытка ввести разные пароль и подтверждение пароля при регистрации")
    public void shouldShowErrorWhenPasswordAndConfirmPasswordNotEquals() {
        open(CFG.frontUrl(), LoginPage.class)
                .clickOnCreateAccountButton()
                .fillingRegistrationPage("Test", "Test", "Test2")
                .checkErrorMessage("Passwords should be equal");
    }

    @Test
    @DisplayName("Попытка зарегестрировать существующего пользователя")
    public void shouldNotRegisterUserWithExistingUsername() {
        String username = "Kirill";
        open(CFG.frontUrl(), LoginPage.class)
                .clickOnCreateAccountButton()
                .fillingRegistrationPage(username, "12345", "12345")
                .checkErrorMessage("Username " + "`" + username + "`" + " already exists");
    }
}
