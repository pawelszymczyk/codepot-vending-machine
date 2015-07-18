package codepot.vendingmachine.domain;

import java.util.Collection;

public interface CoinBank {
    Collection<Coin> change(Transaction transaction);
}
