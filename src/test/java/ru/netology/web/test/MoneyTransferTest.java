package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;
import ru.netology.web.page.LoginPageV2;
import ru.netology.web.page.LoginPageV3;
import ru.netology.web.page.PersonalAccountPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
//    var loginPage = open("http://localhost:9999", LoginPageV1.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV2() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV2();
//    var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV3() {
        var loginPage = open("http://localhost:9999", LoginPageV3.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void GetBalanceTest() {
//        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        PersonalAccountPage page = new PersonalAccountPage();
        System.out.println(page.getCardBalance("92df3f1c-a033-48e6-8390-206f6b1f56c0"));
    }

    @Test
    void moneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        PersonalAccountPage page = new PersonalAccountPage();

        page.moneyTransfer
                (
                        DataHelper.getSecondCardInfo(),
                        1000,
                        DataHelper.getFirstCardInfo()
                );
        assertEquals(11000, page.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(9000, page.getCardBalance(DataHelper.getSecondCardInfo().getId()));
        page.setDefaultBalance();

    }

    @Test
    @DisplayName("Should not transfer negative amount of money")
    void negativeAmountMoneyTransferTest() {
//        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        PersonalAccountPage page = new PersonalAccountPage();
        page.setDefaultBalance();
        page.moneyTransfer
                (
                        DataHelper.getSecondCardInfo(),
                        -1000,
                        DataHelper.getFirstCardInfo()
                );
        assertEquals(11000, page.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(9000, page.getCardBalance(DataHelper.getSecondCardInfo().getId()));
        page.setDefaultBalance();
    }

    @Test
    @DisplayName("Should not transfer money to the wrong card")
    void wrongCardMoneyTransferTest() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        PersonalAccountPage page = new PersonalAccountPage();
        page.setDefaultBalance();
        page.moneyTransfer
                (
                        DataHelper.getWrongCardInfo(),
                        1_000,
                        DataHelper.getFirstCardInfo()
                );
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible);
        $("[data-test-id='action-cancel']").click();
        page.setDefaultBalance();
    }
    @Test
    @DisplayName("Should not transfer money to the same card")
    void sameCardMoneyTransferTest() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        PersonalAccountPage page = new PersonalAccountPage();
        page.setDefaultBalance();
        page.moneyTransfer
                (
                        DataHelper.getFirstCardInfo(),
                        1_000,
                        DataHelper.getFirstCardInfo()
                );
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible);
        $("[data-test-id='action-cancel']").click();
        page.setDefaultBalance();
    }
}
