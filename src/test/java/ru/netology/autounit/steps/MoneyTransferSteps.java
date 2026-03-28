package ru.netology.autounit.steps;

import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.netology.autounit.data.DataHelper;
import ru.netology.autounit.page.DashboardPage;
import ru.netology.autounit.page.LoginPage;
import ru.netology.autounit.page.TransferPage;
import ru.netology.autounit.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferSteps {

    private DashboardPage dashboard;
    private DataHelper.AuthInfo authInfo;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @Допустим("пользователь залогинен с именем {string} и паролем {string}")
    public void login(String username, String password) {
        open("http://localhost:9999");
        authInfo = new DataHelper.AuthInfo(username, password);
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        dashboard = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));

        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою 1 карту с главной страницы")
    public void transferMoney(int amount, String fromCardNumber) {
        DataHelper.CardInfo fromCard = fromCardNumber.equals(firstCard.getNumber()) ? firstCard : secondCard;
        TransferPage transferPage = dashboard.selectCardToTopUp(firstCard);
        dashboard = transferPage.transfer(fromCard, amount);
    }

    @Тогда("баланс его 1 карты из списка на главной странице должен стать {int} рублей")
    public void verifyFirstCardBalance(int expectedBalance) {
        int actualBalance = dashboard.getCardBalance(firstCard);
        assertEquals(expectedBalance, actualBalance);
    }
}