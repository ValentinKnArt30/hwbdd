package ru.netology.autounit.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.autounit.data.DataHelper;
import ru.netology.autounit.page.DashboardPage;
import ru.netology.autounit.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    private DashboardPage dashboard;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        dashboard = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));

        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        int firstBefore = dashboard.getCardBalance(firstCard);
        int secondBefore = dashboard.getCardBalance(secondCard);

        int amount = firstBefore / 2;
        var transferPage = dashboard.selectCardToTopUp(secondCard);
        dashboard = transferPage.validTransfer(firstCard, amount);

        int firstAfter = dashboard.getCardBalance(firstCard);
        int secondAfter = dashboard.getCardBalance(secondCard);

        assertEquals(firstBefore - amount, firstAfter);
        assertEquals(secondBefore + amount, secondAfter);
    }

    @Test
    void shouldNotAllowTransferMoreThanBalance() {
        int firstBefore = dashboard.getCardBalance(firstCard);
        int secondBefore = dashboard.getCardBalance(secondCard);

        int amount = firstBefore + 1000; // больше, чем баланс
        var transferPage = dashboard.selectCardToTopUp(secondCard);
        transferPage.transfer(firstCard, amount);

        transferPage.shouldShowError("Ошибка!");

        int firstAfter = dashboard.getCardBalance(firstCard);
        int secondAfter = dashboard.getCardBalance(secondCard);

        assertEquals(firstBefore, firstAfter);
        assertEquals(secondBefore, secondAfter);
    }

    @Test
    void shouldNotAllowTransferToSameCard() {
        int firstBefore = dashboard.getCardBalance(firstCard);
        int secondBefore = dashboard.getCardBalance(secondCard);

        int amount = firstBefore / 2;
        var transferPage = dashboard.selectCardToTopUp(firstCard);
        transferPage.transfer(firstCard, amount);

        transferPage.shouldShowError("Ошибка!");

        int firstAfter = dashboard.getCardBalance(firstCard);
        int secondAfter = dashboard.getCardBalance(secondCard);

        assertEquals(firstBefore, firstAfter);
        assertEquals(secondBefore, secondAfter);
    }
}