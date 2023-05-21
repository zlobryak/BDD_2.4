package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import ru.netology.web.data.DataHelper.Card;


import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class PersonalAccauntPage {

    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public PersonalAccauntPage() {
    }
    public CardReplenishmentPage cardReplenishment(Card card) {
        $("[data-test-id=92df3f1c-a033-48e6-8390-206f6b1f56c0] button").click();
        return new CardReplenishmentPage();
    }
    public int getCardBalance(String id) {
        // TODO: перебрать все карты и найти по атрибуту data-test-id

        var card = cards.findBy(Condition.attribute
                (
                        "data-test-id",
                        id)).getText();
        return extractBalance(card);
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

}
