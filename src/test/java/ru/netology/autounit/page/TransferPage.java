package ru.netology.autounit.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.autounit.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");

    public TransferPage() {
        amountField.shouldBe(visible);
    }

    public DashboardPage transfer(DataHelper.CardInfo fromCard, int amount) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCard.getNumber());
        transferButton.click();
        return new DashboardPage();
    }
}