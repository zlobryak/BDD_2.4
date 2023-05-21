package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;
import ru.netology.web.page.PersonalAccountPage;

import static com.codeborne.selenide.Selenide.$;
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
        PersonalAccountPage page = new PersonalAccountPage();
        page.moneyTransfer
                (
                        DataHelper.getSecondCardInfo(),
                        1000,
                        DataHelper.getFirstCardInfo()
                );
        assertEquals(11000,
                page.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(9000,
                page.getCardBalance(DataHelper.getSecondCardInfo().getId()));

    }

    @Test
    @DisplayName("Should not transfer negative amount of money")
    void negativeAmountMoneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        PersonalAccountPage page = new PersonalAccountPage();
        page.moneyTransfer
                (
                        DataHelper.getSecondCardInfo(),
                        -1000,
                        DataHelper.getFirstCardInfo()
                );
        assertEquals(11000,
                page.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(9000,
                page.getCardBalance(DataHelper.getSecondCardInfo().getId()));
    }

    @Test
    @DisplayName("Should not transfer money to the wrong card")
    void wrongCardMoneyTransferTest() {
        Configuration.holdBrowserOpen = true;
        PersonalAccountPage page = new PersonalAccountPage();
        page.moneyTransfer
                (
                        DataHelper.getWrongCardInfo(),
                        1_000,
                        DataHelper.getFirstCardInfo()
                );
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible);
        $("[data-test-id='action-cancel']").click();
    }

    @Test
    @DisplayName("Should not transfer money to the same card")
    void sameCardMoneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        PersonalAccountPage page = new PersonalAccountPage();
        page.moneyTransfer
                (
                        DataHelper.getFirstCardInfo(),
                        1_000,
                        DataHelper.getFirstCardInfo()
                );
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should transfer all the money " +
            "from first the card to the second card")
    void transferAllOfTheMoney() {
        PersonalAccountPage page = new PersonalAccountPage();
        page.moneyTransfer
                (
                        DataHelper.getFirstCardInfo(),
                        page.getCardBalance(DataHelper.getFirstCardInfo().getId()),
                        DataHelper.getSecondCardInfo()
                );
        assertEquals(0,
                page.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(20000,
                page.getCardBalance(DataHelper.getSecondCardInfo().getId()));
    }
    @Test
    @DisplayName("Should transfer all the money " +
            "from first the card to the second card")
    void transferAboveTheBalance() {
        PersonalAccountPage page = new PersonalAccountPage();
        page.moneyTransfer
                (
                        DataHelper.getFirstCardInfo(),
                        page.getCardBalance(DataHelper.getFirstCardInfo().getId())+1,
                        DataHelper.getSecondCardInfo()
                );
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible);
    }

}
