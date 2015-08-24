package codepot.vendingmachine.domain;

public class Transaction {

    private Money value = Money.money(0);

    public void add(Coin coin) {
        value = value.add(new Money(coin.getValue()));
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
