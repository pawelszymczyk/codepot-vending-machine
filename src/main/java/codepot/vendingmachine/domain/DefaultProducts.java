package codepot.vendingmachine.domain;

import static codepot.vendingmachine.domain.Money.money;

/**
 * @author bartosz walacik
 */
public enum DefaultProducts implements Product {
    COLA (money(1), "A1"),
    CHIPS(money(0.5), "A2"),
    CANDY(money(0.65), "A3");

    private Money price;
    private final String code;

    DefaultProducts(Money price, String code) {
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
