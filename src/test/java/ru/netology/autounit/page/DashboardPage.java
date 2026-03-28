package ru.netology.autounit.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.autounit.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item div");

    private SelenideElement findCard(String id) {
        cards.first().shouldBe(visible);
        for (SelenideElement card : cards) {
            String cardId = card.getAttribute("data-test-id");
            if (id.equals(cardId)) {
                return card;
            }
        }
        throw new RuntimeException("Карта с id " + id + " не найдена");
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        SelenideElement card = findCard(cardInfo.getId());
        String text = card.getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish).replaceAll("\\s", "");
        return Integer.parseInt(value);
    }

    public TransferPage selectCardToTopUp(DataHelper.CardInfo cardInfo) {
        SelenideElement card = findCard(cardInfo.getId());
        card.$("[data-test-id=action-deposit]").click();
        return new TransferPage();
    }
}