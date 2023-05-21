package ru.netology.web.data;

import lombok.Value;
import ru.netology.web.page.PersonalAccountPage;

public class DataHelper {
    private DataHelper() {}

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }


    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class Card{
        private String number;
        private int balance;
        private String id;
    }
    public static Card FirstCardInfo (String id){
        PersonalAccountPage page = new PersonalAccountPage();
        return new Card("5559 0000 0000 0001", page.getCardBalance(id), id);
    }

    public static Card SecondCardInfo (String id){

        PersonalAccountPage page = new PersonalAccountPage();
        return new Card("5559 0000 0000 0002", page.getCardBalance(id), id );
    }
}