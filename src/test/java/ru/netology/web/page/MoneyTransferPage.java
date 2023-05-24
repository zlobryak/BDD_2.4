package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {
    public void moneyTransfer(DataHelper.Card fromCard, int amount) {
//        $("[data-test-id='" + toCard.getId() + "'] button").click();
//        $("[class='App_appContainer__3jRx1'] h1").shouldHave(Condition.text("Пополнение карты"));
        $("[data-test-id=amount] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),
                Keys.BACK_SPACE);
        $("[data-test-id=amount] input").setValue(String.valueOf(amount));
        $("[data-test-id=from] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),
                Keys.BACK_SPACE);
        $("[data-test-id=from] input").setValue(String.valueOf(fromCard.getNumber()));
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
