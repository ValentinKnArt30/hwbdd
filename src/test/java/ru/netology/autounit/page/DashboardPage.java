package ru.netology.autounit.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.autounit.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        SelenideElement card = $$(".list__item div")
                .findBy(attribute("data-test-id", cardInfo.getId()))
                .shouldBe(visible);
        String text = card.getText();
        return extractBalance(text);
    }

    public TransferPage selectCardToTopUp(DataHelper.CardInfo cardInfo) {
        SelenideElement card = $$(".list__item div")
                .findBy(attribute("data-test-id", cardInfo.getId()))
                .shouldBe(visible);
        card.$("[data-test-id=action-deposit]").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish)
                .replaceAll("\\s", "")
                .replace(",", ".")
                .trim();
        return (int) Double.parseDouble(value);
    }
}