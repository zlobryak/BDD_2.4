package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement toField = $("[data-test-id=to] input");
    public void moneyTransfer(DataHelper.Card fromCard, int amount) {
        amountField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),
                Keys.BACK_SPACE);
        amountField.setValue(String.valueOf(amount));
        fromField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),
                Keys.BACK_SPACE);
        fromField.setValue(String.valueOf(fromCard.getNumber()));
        $("[data-test-id=action-transfer]").click();

    }

    public void errorNotificationPopUp() {
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible);
    }

    public void pushCancelButton() {
        $("[data-test-id='action-cancel']").click();
        $("[class='App_appContainer__3jRx1'] h1").shouldHave(Condition.text("Ваши карты"));
    }
}
