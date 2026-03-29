package ru.netology.autounit.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.autounit.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");

    public TransferPage() {
        amountField.shouldBe(visible);
    }

    public DashboardPage validTransfer(DataHelper.CardInfo fromCard, int amount) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCard.getNumber());
        transferButton.click();
        return new DashboardPage();
    }

    public void transfer(DataHelper.CardInfo fromCard, int amount) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(fromCard.getNumber());
        transferButton.click();
    }

    public void shouldShowError(String expectedText) {
        SelenideElement notification = $("[data-test-id=error-notification]")
                .shouldBe(visible, Duration.ofSeconds(10));
        notification.$(".notification__content")
                .shouldHave(text(expectedText), Duration.ofSeconds(10));
    }
}