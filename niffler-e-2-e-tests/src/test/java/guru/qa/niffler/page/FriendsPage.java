package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;

public class FriendsPage {
    private final SelenideElement
            emptyFriendsTable = $x("//p[text()='There are no users yet']"),
            friendsRequestHeader = $x("//h2[text()='Friend requests']"),
            notEmptyFriendsHeader = $x("//h2[text()='My friends']");

    private final ElementsCollection
            friendsList = $$x("//table//tbody//tr"),
            outcomeRequestsFriendsList = $$x("//span[text()='Waiting...']//ancestor::td//preceding-sibling::td");

    private final String
            tabsStrTemplate = "//h2[text()='%s']//parent::a";

    public FriendsPage goToTab(String tabName) {
        $x(format(tabsStrTemplate, tabName)).click();
        return this;
    }

    public FriendsPage checkThatFriendsTableIsEmpty() {
        emptyFriendsTable.shouldBe(visible);
        return this;
    }

    public FriendsPage checkIncomeRequestsFriendsList(String friendName) {
        friendsRequestHeader.shouldBe(visible);
        friendsList.stream().filter(element -> element.getText().equals(friendName)).findFirst()
                .ifPresent(element -> element.shouldBe(visible).shouldHave(text(friendName))
                        .shouldHave(text("Accept"))
                        .shouldHave(text("Decline")));
        return this;
    }

    public FriendsPage checkOutRequestsFriendsList(String friendName) {
        outcomeRequestsFriendsList.get(0)
                .shouldHave(text(friendName));
        return this;
    }

    public FriendsPage checkThatFriendsTableIsNotEmpty(String friendName) {
        notEmptyFriendsHeader.shouldBe(visible);
        friendsList.stream().filter(element -> element.getText().equals(friendName)).findFirst()
                .ifPresent(element -> element.shouldBe(visible).shouldHave(text(friendName)).shouldHave(text("Unfriend")));
        return this;
    }
}
