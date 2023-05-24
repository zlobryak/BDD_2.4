package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class PersonalAccountPage {

    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private static final ElementsCollection cards = $$(".list__item div");
    private static final String balanceStart = "баланс: ";
    private static final String balanceFinish = " р.";

    public PersonalAccountPage() {
        SelenideElement heading = $("[data-test-id=dashboard]");
        heading.shouldBe(visible);
    }


    public int getCardBalance(DataHelper.Card CardInfo) {
        return extractBalance
                (
                        cards.findBy(Condition.attribute
                                ("data-test-id", CardInfo.getId())).getText()
                );
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }


    public void chooseCard(DataHelper.Card toCard) {
        $("[data-test-id='" + toCard.getId() + "'] button").click();
        $("[class='App_appContainer__3jRx1'] h1").shouldHave(Condition.text("Пополнение карты"));
    }
}