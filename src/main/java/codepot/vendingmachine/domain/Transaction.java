package codepot.vendingmachine.domain;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Transaction {

    private Money value = Money.money(0);

    public void add(Coin coin) {
        value.add(new Money(coin.getValue()));
    }

    public Money getValue() {
        return value;
    }

    public boolean isZero() {
        return value.isZero();
    }

    public void reduce(Money productPrice) {
        value = value.substract(productPrice);
    }
}
