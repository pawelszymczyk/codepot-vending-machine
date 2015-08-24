package codepot.vendingmachine.infrastructure.suncorp;

import codepot.vendingmachine.domain.Money;
import codepot.vendingmachine.domain.Product;

import static codepot.vendingmachine.domain.Money.money;

public enum SunCorpProducts implements Product {
    SALAD (money(0.5), "S1"),
    CARROT(money(0.5), "C2"),
    BURGER(money(1), "B3"),
    PADTHAI(money(1), "PT4");

    private Money price;
    private final String code;

    SunCorpProducts(Money price, String code) {
        this.price = price;
        this.code = code;
    }

    @Override
    public Money getPrice() {
        return price;
    }

    @Override
    public String getCode() {
        return code;
    }
}
