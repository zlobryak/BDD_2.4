package ru.netology.web.test;

//import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;
import ru.netology.web.page.MoneyTransferPage;
import ru.netology.web.page.PersonalAccountPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    @BeforeEach
    public void openNadLogin() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @BeforeEach
    public void setDefaultBalance() {
        MoneyTransferPage moneyTransferPage = new MoneyTransferPage();
        PersonalAccountPage personalAccountPage = new PersonalAccountPage();
        int firstBalance = personalAccountPage.getCardBalance(DataHelper.getFirstCardInfo());
        int secondBalance = personalAccountPage.getCardBalance(DataHelper.getSecondCardInfo());
        if (firstBalance >= secondBalance) {
            int deltaBalance = 10_000 - secondBalance;
            personalAccountPage.chooseCard(DataHelper.getSecondCardInfo());
            moneyTransferPage.moneyTransfer(
                    DataHelper.getFirstCardInfo(),
                    deltaBalance);
        } else {
            int deltaBalance = 10_000 - firstBalance;
            personalAccountPage.chooseCard(DataHelper.getFirstCardInfo());
            moneyTransferPage.moneyTransfer(
                    DataHelper.getSecondCardInfo(),
                    deltaBalance);
        }
        new PersonalAccountPage();
    }

    @Test
    @DisplayName("Should transfer money from second card to first")
    void moneyTransferTest() {
//                Configuration.holdBrowserOpen = true;
        PersonalAccountPage personalAccountPage = new PersonalAccountPage();
        MoneyTransferPage moneyTransferPage = new MoneyTransferPage();
        personalAccountPage.chooseCard(DataHelper.getFirstCardInfo());

        moneyTransferPage.moneyTransfer(DataHelper.getSecondCardInfo(), 1000);

        assertEquals(11000, personalAccountPage
                .getCardBalance(DataHelper.getFirstCardInfo()));
        assertEquals(9000, personalAccountPage
                .getCardBalance(DataHelper.getSecondCardInfo()));

    }

    @Test
    @DisplayName("Should not transfer negative amount of money")
    void negativeAmountMoneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        PersonalAccountPage personalAccountPage = new PersonalAccountPage();
        MoneyTransferPage moneyTransferPage = new MoneyTransferPage();
        personalAccountPage.chooseCard(DataHelper.getFirstCardInfo());

        moneyTransferPage.moneyTransfer(DataHelper.getSecondCardInfo(), -1000);

        assertEquals(11000, personalAccountPage
                        .getCardBalance(DataHelper.getFirstCardInfo()));
        assertEquals(9000, personalAccountPage
                        .getCardBalance(DataHelper.getSecondCardInfo()));
    }

    @Test
    @DisplayName("Should not transfer money to the wrong card")
    void wrongCardMoneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        PersonalAccountPage personalAccountPage = new PersonalAccountPage();
        MoneyTransferPage moneyTransferPage = new MoneyTransferPage();
        personalAccountPage.chooseCard(DataHelper.getFirstCardInfo());

        moneyTransferPage.moneyTransfer(DataHelper.getWrongCardInfo(), 1000);

        moneyTransferPage.errorNotificationPopUp();
        moneyTransferPage.pushCancelButton();
    }

    @Test
    @DisplayName("Should not transfer money to the same card")
    void sameCardMoneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        MoneyTransferPage moneyTransferPage = new MoneyTransferPage();
        PersonalAccountPage personalAccountPage = new PersonalAccountPage();
        personalAccountPage.chooseCard(DataHelper.getFirstCardInfo());

        moneyTransferPage.moneyTransfer(DataHelper.getFirstCardInfo(), 1_000);

        moneyTransferPage.errorNotificationPopUp();
        moneyTransferPage.pushCancelButton();
    }

    @Test
    @DisplayName("Should transfer all the money " +
            "from first the card to the second card")
    void transferAllOfTheMoney() {
//                Configuration.holdBrowserOpen = true;
        MoneyTransferPage moneyTransferPage = new MoneyTransferPage();
        PersonalAccountPage personalAccountPage = new PersonalAccountPage();
        int balance = personalAccountPage.getCardBalance
                (DataHelper.getFirstCardInfo());

        personalAccountPage.chooseCard(DataHelper.getSecondCardInfo());
        moneyTransferPage.moneyTransfer(DataHelper.getFirstCardInfo(),balance);

        assertEquals(0, personalAccountPage
                .getCardBalance(DataHelper.getFirstCardInfo()));
        assertEquals(20000, personalAccountPage
                .getCardBalance(DataHelper.getSecondCardInfo()));
    }

    @Test
    @DisplayName("Should not transfer more money")
    void transferAboveTheBalance() {
//        Configuration.holdBrowserOpen = true;
        PersonalAccountPage personalAccountPage = new PersonalAccountPage();
        MoneyTransferPage moneyTransferPage = new MoneyTransferPage();
        int balance = personalAccountPage.getCardBalance(DataHelper.getFirstCardInfo());

        personalAccountPage.chooseCard(DataHelper.getSecondCardInfo());
        moneyTransferPage.moneyTransfer(DataHelper.getFirstCardInfo(),
                balance + 1); //Переводит на 1 рубль больше, чем есть на балласне карты.

        moneyTransferPage.errorNotificationPopUp();
        moneyTransferPage.pushCancelButton();
    }

}
