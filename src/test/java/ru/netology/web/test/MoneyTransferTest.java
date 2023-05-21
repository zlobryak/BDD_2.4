package ru.netology.web.test;

import com.codeborne.selenide.Condition;
//import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
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
    PersonalAccountPage page = new PersonalAccountPage();
    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        page.setDefaultBalance();
    }
    @AfterEach
    void defaultBalance(){
        page.setDefaultBalance();
    }
    @Test
    @DisplayName("Should transfer money from the second card to the first card")
    void moneyTransferTest() {
        page.moneyTransfer
                (
                        DataHelper.getSecondCardInfo(),
                        1000,
                        DataHelper.getFirstCardInfo()
                );
        assertEquals(11000, page.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(9000, page.getCardBalance(DataHelper.getSecondCardInfo().getId()));
    }

    @Test
    @DisplayName("Should not transfer negative amount of money")
    void negativeAmountMoneyTransferTest() {        page.moneyTransfer
                (
                        DataHelper.getSecondCardInfo(),
                        -1000,
                        DataHelper.getFirstCardInfo()
                );
        assertEquals(11000, page.getCardBalance(DataHelper.getFirstCardInfo().getId()));
        assertEquals(9000, page.getCardBalance(DataHelper.getSecondCardInfo().getId()));
    }

    @Test
    @DisplayName("Should not transfer money to the wrong card")
    void wrongCardMoneyTransferTest() {
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
    }

    @Test
    @DisplayName("Should not transfer money to the same card")
    void sameCardMoneyTransferTest() {
        page.moneyTransfer
                (
                        DataHelper.getFirstCardInfo(),
                        1_000,
                        DataHelper.getFirstCardInfo()
                );
        $("[data-test-id='error-notification']")
                .shouldBe(Condition.visible);
        $("[data-test-id='action-cancel']").click();
    }
}
