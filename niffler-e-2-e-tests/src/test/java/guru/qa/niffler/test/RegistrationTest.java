package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class RegistrationTest {
    private final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Успешная регистрация")
    public void modalWindowShodBeVisibleAfterSuccessRegistration() {
        Faker faker = new Faker();
        final String username = faker.name().username();
        open(CFG.frontUrl(), LoginPage.class)
                .clickOnCreateAccountButton()
                .checkHeaderText("Sign up")
                .fillingRegistrationPage(username, "12345", "12345")
                .checkSuccessRegistrationMessageAndGoToSignInPage()
                .checkLoginHeader("Log in");
    }

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
