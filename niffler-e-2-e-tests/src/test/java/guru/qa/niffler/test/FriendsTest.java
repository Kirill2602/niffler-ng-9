package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.extension.UserQueueExtension.UserType.Type.*;

public class FriendsTest {
    private static final Config CFG = Config.getInstance();

    @ExtendWith(UserQueueExtension.class)
    @Test
    void friendsShouldNotBePresentInFriendsTable(@UserQueueExtension.UserType(EMPTY) UserQueueExtension.StaticUser user) {
        FriendsPage friendsPage = new FriendsPage();
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .goToProfile("Friends");
        friendsPage
                .goToTab("Friends")
                .checkThatFriendsTableIsEmpty();
    }

    @ExtendWith(UserQueueExtension.class)
    @Test
    void shouldBeVisibleIncomeRequestInFriendsTable(@UserQueueExtension.UserType(WITH_INCOME_REQUEST) UserQueueExtension.StaticUser user) {
        FriendsPage friendsPage = new FriendsPage();
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .goToProfile("Friends");
        friendsPage
                .goToTab("Friends")
                .checkIncomeRequestsFriendsList(user.income());
    }

    @ExtendWith(UserQueueExtension.class)
    @Test
    void shouldBeVisibleOutcomeRequestInAllPeoplesList(@UserQueueExtension.UserType(WITH_OUTCOME_REQUEST) UserQueueExtension.StaticUser user) {
        FriendsPage friendsPage = new FriendsPage();
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .goToProfile("Friends");
        friendsPage
                .goToTab("All people")
                .checkOutRequestsFriendsList(user.outcome());
    }

    @ExtendWith(UserQueueExtension.class)
    @Test
    void shouldBeVisibleFriendsInFriendsList(@UserQueueExtension.UserType(WITH_FRIEND) UserQueueExtension.StaticUser user) {
        FriendsPage friendsPage = new FriendsPage();
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .goToProfile("Friends");
        friendsPage
                .goToTab("Friends")
                .checkThatFriendsTableIsNotEmpty(user.friend());
    }
}
