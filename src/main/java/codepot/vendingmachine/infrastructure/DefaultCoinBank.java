package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.Money;
import codepot.vendingmachine.domain.Transaction;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class DefaultCoinBank implements CoinBank {

    private final Set<Coin> coins = Sets.newHashSet(
            Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY, Coin.PENNY,
            Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL, Coin.NICKEL,
            Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME, Coin.DIME,
            Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER
    );

    public Collection<Coin> change(Transaction transaction) {
        List<Coin> changeCoins = new ArrayList<>();

        while (!transaction.isZero() || coins.isEmpty()) {
            Coin coin = findMaxButLessThan(transaction.getValue());
            changeCoins.add(coin);
        }

        return changeCoins;
    }

    private Coin findMaxButLessThan(Money balance) {
        Set<Coin> copyOfCoins = Sets.newHashSet(coins);
        for (Coin coinFromTreasure : copyOfCoins) {
            if (balance.greaterOrEquals(coinFromTreasure.getMoney())) {
                coins.remove(coinFromTreasure);
                return coinFromTreasure;
            }
        }
        return null;
    }
}
