package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.Money;
import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.integration.OutOfMoneyNotifier;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DefaultCoinBank implements CoinBank {

    private final List<OutOfMoneyNotifier> outOfMoneyNotifiers;
    private final List<Coin> coins = new ArrayList<>();

    public DefaultCoinBank(List<OutOfMoneyNotifier> outOfMoneyNotifiers) {
        this(outOfMoneyNotifiers,
                Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY,
                Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL,
                Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME,
                Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER
        );
    }

    public DefaultCoinBank(List<OutOfMoneyNotifier> outOfMoneyNotifiers, Coin... coins) {
        this.outOfMoneyNotifiers = outOfMoneyNotifiers;
        this.coins.addAll(Lists.newArrayList(coins));
    }

    @Override
    public Collection<Coin> change(Transaction transaction) {
        List<Coin> changeCoins = new ArrayList<>();

        while (!(transaction.isZero() || coins.isEmpty())) {
            findMaxButLessThan(transaction.getValue()).ifPresent(c -> {
                transaction.reduce(c.getMoney());
                changeCoins.add(c);
            });
        }

        if (coins.isEmpty()) {
            outOfMoneyNotifiers.forEach(n -> n.sendSupplyRequestToVendor());
        }

        return changeCoins;
    }

    private Optional<Coin> findMaxButLessThan(Money balance) {
        List<Coin> copyOfCoins = Lists.newArrayList(coins);
        copyOfCoins.sort((o1, o2) -> -1* o1.getValue().compareTo(o2.getValue()));

        for (Coin coinFromTreasure : copyOfCoins) {
            if (balance.greaterOrEquals(coinFromTreasure.getMoney())) {
                coins.remove(coinFromTreasure);
                return Optional.of(coinFromTreasure);
            }
        }
        return Optional.empty();
    }
}
