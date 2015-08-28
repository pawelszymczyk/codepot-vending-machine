package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CoinBank {

    private final ServiceNotifier notifier;
    private final List<Coin> coins = new ArrayList<>();

    @Inject
    public CoinBank(@Named("smsServiceNotifier") ServiceNotifier notifier) {
        this(notifier,
                Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY,
                Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL,
                Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME,
                Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER
        );
    }

    public CoinBank(ServiceNotifier notifier, Coin... coins) {
        this.notifier = notifier;
        this.coins.addAll(Lists.newArrayList(coins));
    }

    public Collection<Coin> change(Transaction transaction) {
        List<Coin> changeCoins = new ArrayList<>();

        while (!(transaction.isZero() || coins.isEmpty())) {
            findMaxButLessThan(transaction.getValue()).ifPresent(c -> {
                transaction.reduce(c.getMoney());
                changeCoins.add(c);
            });
        }

        if (coins.isEmpty()) {
            notifier.doNotify();
        }

        return changeCoins;
    }

    private Optional<Coin> findMaxButLessThan(Money balance) {
        List<Coin> copyOfCoins = Lists.newArrayList(coins);
        copyOfCoins.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        for (Coin coinFromTreasure : copyOfCoins) {
            if (balance.greaterOrEquals(coinFromTreasure.getMoney())) {
                coins.remove(coinFromTreasure);
                return Optional.of(coinFromTreasure);
            }
        }
        return Optional.empty();
    }
}
