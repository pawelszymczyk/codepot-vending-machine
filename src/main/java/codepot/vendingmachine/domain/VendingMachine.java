package codepot.vendingmachine.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static codepot.vendingmachine.domain.Money.money;

public class VendingMachine {

    private Set<Coin> coinReturnTray;

    public VendingMachine() {
        coinReturnTray = new HashSet<>();
    }

    public String getDisplay() {
        if (getBalance().isZero()) {
            return "INSERT A COIN";
        }

        return "zonk";
    }

    /**
     * Current amount on display:
     * sum of *valid* coins inserted, minus sold products, minus change
     */
    public Money getBalance() {
        return money(0);
    }

    /**
     * @return unmodifiableSet
     */
    public Set<Coin> getCoinReturnTray() {
        return Collections.unmodifiableSet(coinReturnTray);
    }
}
