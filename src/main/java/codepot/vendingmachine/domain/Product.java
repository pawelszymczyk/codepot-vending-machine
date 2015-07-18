package codepot.vendingmachine.domain;

import static codepot.vendingmachine.domain.Money.money;

/**
 * @author bartosz walacik
 */
public enum Product {
    COLA (money(1)),
    CHIPS(money(0.5)),
    CANDY(money(0.65));

    private Money price;

    Product(Money price) {
        this.price = price;
    }

    public Money getPrice() {
        return price;
    }
}
