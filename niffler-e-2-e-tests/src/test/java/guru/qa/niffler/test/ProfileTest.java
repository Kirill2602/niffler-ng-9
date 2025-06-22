package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class ProfileTest {
    private final Config CFG = Config.getInstance();

    @Category(
            name = "Test category",
            username = "duck",
            archived = false
    )
    @Test
    @DisplayName("Архивация категории")
    public void checkThatCategoryIsArchived(CategoryJson categoryJson) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("duck", "12345")
                .submit()
                .goToProfile("Profile")
                .checkVisibilityOfProfileHeader()
                .clickOnButton(categoryJson.name(), "Archive category")
                .checkConfirmText("Do you really want to archive " + categoryJson.name() + "? After this change it won't be available while creating spends")
                .clickOnArchiveConfirmButton()
                .clickOnArchivedToggle()
                .checkCategoryStatus(categoryJson.name());
    }

    @Category(
            name = "1",
            username = "Kirill",
            archived = true
    )
    @Test
    @DisplayName("Восстановление категории из архива")
    public void checkThatCategoryIsUnArchive(CategoryJson categoryJson) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("Kirill", "12345")
                .submit()
                .goToProfile("Profile")
                .checkVisibilityOfProfileHeader()
                .clickOnArchivedToggle()
                .checkCategoryStatus(categoryJson.name())
                .clickOnUnArchiveButton(categoryJson.name())
                .checkConfirmText("Do you really want to unarchive category " + categoryJson.name() + "?")
                .clickOnUnArchiveConfirmButton()
                .clickOnArchivedToggle()
                .checkCategoryStatus(categoryJson.name());
    }
}
