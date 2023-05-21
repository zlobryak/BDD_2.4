package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper.Card;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class PersonalAccountPage {

    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement heading = $("[data-test-id=dashboard]");

    public PersonalAccountPage() {
        heading.shouldBe(visible);
    }

    public void moneyTransfer(Card fromCard, int amount, Card toCard) {
        $("[data-test-id='" + toCard.getId() + "'] button").click();
        $("[data-test-id=amount] input").setValue(String.valueOf(amount));
        $("[data-test-id=from] input").setValue(String.valueOf(fromCard.getNumber()));
        $("[data-test-id=action-transfer]").click();
    }

    public int getCardBalance(String id) {
        return extractBalance
                (
                        cards.findBy(Condition.attribute("data-test-id", id)).getText()
                );
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
