package codepot.vendingmachine.domain;

import com.google.common.annotations.VisibleForTesting;

import java.util.Optional;
import java.util.Set;

public class VendingMachine {

    public VendingMachine() {
    }

    public String getDisplay() {
        return "INSERT A COIN";
    }

    public void selectProduct(String productCode) {

    }

    public void insertCoin(Coin coin) {

    }

    public void cancel() {

    }

    public Set<Coin> getCoinReturnTray() {
        return null;
    }

    @VisibleForTesting
    public Optional<Transaction> getCurrentTransaction() {
        return null;
    }
}
