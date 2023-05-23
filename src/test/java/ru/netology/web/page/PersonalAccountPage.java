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


    public static int getCardBalance(String id) {
        return extractBalance
                (
                        cards.findBy(Condition.attribute("data-test-id", id)).getText()
                );
    }

    private static int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void setDefaultBalance() {
        // Этот метод реализован для зауска перед каждым тестом,
        // он приводит значение баллана обоих карт к стандартному
        int firstBalance = getCardBalance(DataHelper.getFirstCardInfo().getId());
        int secondBalance = getCardBalance(DataHelper.getSecondCardInfo().getId());
        if (firstBalance >= secondBalance) {
            int deltaBalance = 10_000 - secondBalance;
            MoneyTransferPage.moneyTransfer(
                    DataHelper.getFirstCardInfo(),
                    deltaBalance,
                    DataHelper.getSecondCardInfo());
        } else {
            int deltaBalance = 10_000 - firstBalance;
            MoneyTransferPage.moneyTransfer(
                    DataHelper.getSecondCardInfo(),
                    deltaBalance,
                    DataHelper.getFirstCardInfo());
        }
        new PersonalAccountPage();
    }
}