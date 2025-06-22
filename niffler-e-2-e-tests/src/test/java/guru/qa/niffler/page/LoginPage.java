package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginPage {
    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            createNewAccountButton = $("#register-button"),
            loginHeader = $x("//h1[@class='header']"),
            errorMessageBlock = $x("//p[@class='form__error']");

    public LoginPage fillLoginPage(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        return this;
    }

    public MainPage submit() {
        submitButton.click();
        return new MainPage();
    }

    public RegistrationPage clickOnCreateAccountButton() {
        createNewAccountButton.click();
        return new RegistrationPage();
    }

    public LoginPage checkLoginHeader(String headerText) {
        assertEquals(headerText, loginHeader.getText());
        return this;
    }

    public LoginPage checkErrorMessageBlock(String errorMessageText) {
        assertEquals(errorMessageText, errorMessageBlock.getText());
        return this;
    }
}
