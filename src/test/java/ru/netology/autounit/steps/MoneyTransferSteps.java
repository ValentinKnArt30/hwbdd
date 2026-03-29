package ru.netology.autounit.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.netology.autounit.data.DataHelper;
import ru.netology.autounit.page.DashboardPage;
import ru.netology.autounit.page.LoginPage;
import ru.netology.autounit.page.TransferPage;
import ru.netology.autounit.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.assertEquals;

public class MoneyTransferSteps {

    private DashboardPage dashboard;
    private DataHelper.AuthInfo authInfo;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @Дано("пользователь залогинен с именем {string} и паролем {string}")
    public void login(String username, String password) {
        open("http://localhost:9999");
        authInfo = new DataHelper.AuthInfo(username, password);
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        dashboard = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));

        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы")
    public void transferMoney(int amount, String fromCardNumber, int toCardIndex) {
        DataHelper.CardInfo fromCard;
        if (fromCardNumber.equals(firstCard.getNumber())) {
            fromCard = firstCard;
        } else if (fromCardNumber.equals(secondCard.getNumber())) {
            fromCard = secondCard;
        } else {
            throw new IllegalArgumentException("Неизвестная карта отправителя: " + fromCardNumber);
        }

        DataHelper.CardInfo toCard = (toCardIndex == 1) ? firstCard : secondCard;

        TransferPage transferPage = dashboard.selectCardToTopUp(toCard);
        dashboard = transferPage.validTransfer(fromCard, amount);
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей")
    public void verifyCardBalance(int cardIndex, int expectedBalance) {
        DataHelper.CardInfo card = (cardIndex == 1) ? firstCard : secondCard;
        int actualBalance = dashboard.getCardBalance(card);
        assertEquals(expectedBalance, actualBalance);
    }
}