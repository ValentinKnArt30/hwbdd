package ru.netology.autounit.test;

import org.junit.jupiter.api.Test;
import ru.netology.autounit.data.DataHelper;
import ru.netology.autounit.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboard = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));

        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();

        int firstCardBalanceBefore = dashboard.getCardBalance(firstCard);
        int secondCardBalanceBefore = dashboard.getCardBalance(secondCard);

        var transferPage = dashboard.selectCardToTopUp(secondCard);
        int amount = firstCardBalanceBefore / 2;
        dashboard = transferPage.transfer(firstCard, amount);
    }

    @Test
    void shouldNotAllowTransferMoreThanBalance() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboard = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));

        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();

        int firstCardBalance = dashboard.getCardBalance(firstCard);

        var transferPage = dashboard.selectCardToTopUp(secondCard);
        int amount = firstCardBalance + 1000;

        transferPage.transfer(firstCard, amount);
    }

    @Test
    void shouldNotAllowTransferToSameCard() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboard = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));

        var firstCard = DataHelper.getFirstCard();

        var transferPage = dashboard.selectCardToTopUp(firstCard);
        int amount = 500;

        transferPage.transfer(firstCard, amount);
        int firstCardBalanceAfter = dashboard.getCardBalance(firstCard);
        assertEquals(dashboard.getCardBalance(firstCard), firstCardBalanceAfter);
    }
}