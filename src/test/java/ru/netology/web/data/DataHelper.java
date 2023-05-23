package ru.netology.web.data;

import lombok.Value;
import ru.netology.web.page.PersonalAccountPage;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }


    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class Card {
        String number;
        String id;
    }

    public static Card getFirstCardInfo() {
        return new Card
                (
                        "5559 0000 0000 0001",
                        "92df3f1c-a033-48e6-8390-206f6b1f56c0"
                );
    }

    public static Card getSecondCardInfo() {
        return new Card
                (
                        "5559 0000 0000 0002",
                        "0f3f5c2a-249e-4c3d-8287-09f7a039391d"
                );
    }

    public static Card getWrongCardInfo() {
        return new Card
                (
                        "5559 0000 0000 0003",
                        "WrongID"
                );
    }
}