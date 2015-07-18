package codepot.vendingmachine.domain;

import java.math.BigDecimal;

import static codepot.vendingmachine.domain.Money.money;

/**
 * @author bartosz walacik
 */
public enum Coin {
    /** 1 cent coin */
    PENNY (money(0.01), 1),

    /** 5 cents coin */
    NICKEL (money(0.05), 1),

    /** 10 cents coin */
    DIME   (money(0.1), 2),

    /** 25 cents coin */
    QUARTER(money(0.25), 3),
    ;

    private Money money;
    private int   weight;

    Coin(Money money, int weight) {
        this.money = money;
        this.weight = weight;
    }

    public Money getMoney() {
        return money;
    }

    public BigDecimal getValue() {
        return money.getValue();
    }

    public int getWeight() {
        return weight;
    }
}
