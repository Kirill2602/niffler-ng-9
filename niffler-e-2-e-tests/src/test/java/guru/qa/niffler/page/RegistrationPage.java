package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RegistrationPage {
    private final SelenideElement
            registrationHeader = $x("//h1[@class='header']"),
            usernameField = $("#username"),
            passwordField = $("#password"),
            showPasswordButton = $("#passwordBtn"),
            submitPasswordField = $("#passwordSubmit"),
            showSubmitPasswordButton = $("#passwordSubmitBtn"),
            registerButton = $("#register-button"),
            successRegistrationMessage = $x("//p[text()=\"Congratulations! You've registered!\"]"),
            goToSignInButton = $x("//a[text()='Sign in']"),
            errorMessageBlock = $x("//span[@class='form__error']");

    public RegistrationPage checkHeaderText(String headerText) {
        registrationHeader.shouldHave(text(headerText));
        return this;
    }

    public RegistrationPage fillingRegistrationPage(String username, String password, String passwordConfirmation) {
        setUsernameField(username);
        setPasswordField(password);
        setSubmitPasswordField(passwordConfirmation);
        clickOnRegistrationButton();
        return this;
    }

    public LoginPage checkSuccessRegistrationMessageAndGoToSignInPage() {
        checkVisibilityOfSuccessRegistrationMessage();
        goToSignInButton.click();
        return new LoginPage();
    }

    public RegistrationPage clickOnRegistrationButton() {
        registerButton.click();
        return this;
    }

    public RegistrationPage checkErrorMessage(String errorMessage) {
        errorMessageBlock.shouldHave(text(errorMessage));
        return this;
    }

    private void checkVisibilityOfSuccessRegistrationMessage() {
        successRegistrationMessage.shouldBe(visible);
    }

    private void setUsernameField(String username) {
        usernameField.setValue(username);
    }

    private void setPasswordField(String password) {
        passwordField.setValue(password);
    }

    private void setSubmitPasswordField(String submitPassword) {
        submitPasswordField.setValue(submitPassword);
    }
}
