package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class ProfileTest {
    private final Config CFG = Config.getInstance();

    @User(
            username = "duck",
            categories = {@Category(name = "Test category")}
    )

    @Test
    @DisplayName("Архивация категории")
    public void checkThatCategoryIsArchived(CategoryJson categoryJson) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(categoryJson.username(), "12345")
                .submit()
                .goToProfile("Profile")
                .checkVisibilityOfProfileHeader()
                .clickOnButton(categoryJson.name(), "Archive category")
                .checkConfirmText("Do you really want to archive " + categoryJson.name() + "? After this change it won't be available while creating spends")
                .clickOnArchiveConfirmButton()
                .clickOnArchivedToggle()
                .checkCategoryStatus(categoryJson.name());
    }

    @User(
            categories = {@Category(name = "1", archived = true)}
    )
    @Test
    @DisplayName("Восстановление категории из архива")
    public void checkThatCategoryIsUnArchive(CategoryJson categoryJson) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(categoryJson.username(), "12345")
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
