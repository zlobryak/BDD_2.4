package ru.netology.web.page;

import ru.netology.web.data.DataHelper.Card;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PersonalAccauntPage {
    public CardReplenishmentPage cardReplenishment(Card) {
        $(by("[data-test-id]", "92df3f1c-a033-48e6-8390-206f6b1f56c0"));
        return new CardReplenishmentPage();
    }
    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public PersonalAccauntPage() {
    }
    public CardReplenishmentPage cardReplenishment(Card) {
        $("[data-test-id=92df3f1c-a033-48e6-8390-206f6b1f56c0] button").click();
        return new CardReplenishmentPage();
    }
    public int getCardBalance(String id) {
        // TODO: перебрать все карты и найти по атрибуту data-test-id
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        String start = String.valueOf(text.indexOf(balanceStart));
        String finish = String.valueOf(text.indexOf(balanceFinish));
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

}
