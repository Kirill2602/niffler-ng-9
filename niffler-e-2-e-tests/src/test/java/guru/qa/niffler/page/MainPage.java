package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;

public class MainPage {
    private final SelenideElement
            spendingTable = $("#spendings"),
            profileIcon = $x("//*[@data-testid=\"PersonIcon\"]//ancestor::button");

    private final String
            profileDropdownItemsStrTemplate = "//a[text()='%s']";

    public MainPage checkThatPageLoaded() {
        spendingTable.should(visible);
        return this;
    }

    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description))
                .$$("td")
                .get(5)
                .click();
        return new EditSpendingPage();
    }

    public MainPage checkThatTableContainsSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description))
                .should(visible);
        return this;
    }

    public ProfilePage goToProfile(String itemName) {
        profileIcon.click();
        $x(format(profileDropdownItemsStrTemplate, itemName)).click();
        return new ProfilePage();
    }
}
