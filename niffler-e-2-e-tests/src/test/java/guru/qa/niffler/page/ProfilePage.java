package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

public class ProfilePage {
    private final SelenideElement
            profileHeader = $x("//h2[text()='Profile']"),
            archivedToggle = $x("//input[@type='checkbox']"),
            confirmWindowHeader = $x("//p[@id='alert-dialog-slide-description']"),
            closeConfirmButton = $x("//button[text()='Close']"),
            archiveConfirmButton = $x("//button[text()='Archive']"),
            unArchiveConfirmButton = $x("//button[text()='Unarchive']");


    private final String
            categoryButtonsStrTemplate = "//span[text()='%s']//parent::div//following-sibling::div//button[@aria-label='%s']",
            categoryListStrTemplate = "//span[text()='%s']",
            unArchiveButtonStrTemplate = "//span[text()='%s']//parent::div//following-sibling::span[@aria-label='Unarchive category']";

    public ProfilePage checkVisibilityOfProfileHeader() {
        profileHeader.shouldBe(visible);
        return this;
    }

    public ProfilePage clickOnArchivedToggle() {
        executeJavaScript("window.scrollTo(0,0)");
        archivedToggle.click();
        return this;
    }

    public ProfilePage clickOnButton(String categoryName, String buttonName) {
        $$x(format(categoryButtonsStrTemplate, categoryName, buttonName)).get(0).click();
        return this;
    }

    public ProfilePage checkConfirmText(String confirmText) {
        confirmWindowHeader.shouldHave(text(confirmText));
        return this;
    }

    public ProfilePage clickOnArchiveConfirmButton() {
        archiveConfirmButton.click();
        return this;
    }

    public ProfilePage clickOnCloseConfirmButton() {
        closeConfirmButton.click();
        return this;
    }

    public ProfilePage checkCategoryStatus(String categoryName) {
        $x(format(categoryListStrTemplate, categoryName)).shouldBe(visible);
        return this;
    }

    public ProfilePage clickOnUnArchiveButton(String categoryName) {
        $x(format(unArchiveButtonStrTemplate, categoryName)).click();
        return this;
    }

    public ProfilePage clickOnUnArchiveConfirmButton() {
        unArchiveConfirmButton.click();
        return this;
    }
}
