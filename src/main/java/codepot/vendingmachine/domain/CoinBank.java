package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.SmsServiceNotifier;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CoinBank {

    private final List<Coin> coins = new ArrayList<>();
    private final ServiceNotifier serviceNotifier;

    public CoinBank(SmsServiceNotifier serviceNotifier) {
        this(serviceNotifier,
                Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY,
                Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL,
                Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME,
                Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER
        );
    }

    CoinBank(SmsServiceNotifier serviceNotifier, Coin... coins) {
        this.serviceNotifier = serviceNotifier;
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
            serviceNotifier.doNotify();
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
