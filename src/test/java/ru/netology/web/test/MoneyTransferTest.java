package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
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
        PersonalAccountPage page = new PersonalAccountPage();
        page.setDefaultBalance();
    }

    @Test
    @DisplayName("Should transfer money from second card to first")
    void moneyTransferTest() {
        //        Configuration.holdBrowserOpen = true;
        MoneyTransferPage.moneyTransfer
                (
                        DataHelper.getSecondCardInfo(),
                        1000,
                        DataHelper.getFirstCardInfo()
                );
        assertEquals(11000,
                PersonalAccountPage.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(9000,
                PersonalAccountPage.getCardBalance(DataHelper.getSecondCardInfo().getId()));

    }

    @Test
    @DisplayName("Should not transfer negative amount of money")
    void negativeAmountMoneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        MoneyTransferPage.moneyTransfer
                (
                        DataHelper.getSecondCardInfo(),
                        -1000,
                        DataHelper.getFirstCardInfo()
                );
        assertEquals(11000,
                PersonalAccountPage.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(9000,
                PersonalAccountPage.getCardBalance(DataHelper.getSecondCardInfo().getId()));
    }

    @Test
    @DisplayName("Should not transfer money to the wrong card")
    void wrongCardMoneyTransferTest() {
        Configuration.holdBrowserOpen = true;
        MoneyTransferPage.moneyTransfer
                (
                        DataHelper.getWrongCardInfo(),
                        1_000,
                        DataHelper.getFirstCardInfo()
                );
        MoneyTransferPage.errorNotificationPopUp();
        MoneyTransferPage.pushCancelButton();
    }

    @Test
    @DisplayName("Should not transfer money to the same card")
    void sameCardMoneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        MoneyTransferPage.moneyTransfer
                (
                        DataHelper.getFirstCardInfo(),
                        1_000,
                        DataHelper.getFirstCardInfo()
                );
        MoneyTransferPage.errorNotificationPopUp();
        MoneyTransferPage.pushCancelButton();
    }

    @Test
    @DisplayName("Should transfer all the money " +
            "from first the card to the second card")
    void transferAllOfTheMoney() {
        MoneyTransferPage.moneyTransfer
                (
                        DataHelper.getFirstCardInfo(),
                        PersonalAccountPage.getCardBalance(DataHelper.getFirstCardInfo().getId()),
                        DataHelper.getSecondCardInfo()
                );
        assertEquals(0,
                PersonalAccountPage.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(20000,
                PersonalAccountPage.getCardBalance(DataHelper.getSecondCardInfo().getId()));
    }

    @Test
    @DisplayName("Should not transfer more money")
    void transferAboveTheBalance() {
        MoneyTransferPage.moneyTransfer
                (
                        DataHelper.getFirstCardInfo(),
                        PersonalAccountPage.getCardBalance(DataHelper.getFirstCardInfo().getId()) + 1,
                        DataHelper.getSecondCardInfo()
                );
        MoneyTransferPage.errorNotificationPopUp();
        MoneyTransferPage.pushCancelButton();
    }

}
