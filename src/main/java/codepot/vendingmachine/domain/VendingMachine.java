package codepot.vendingmachine.domain;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class VendingMachine {

    private final Set<Product> productsTray = new HashSet<>();
    private final Set<Coin> coinReturnTray = new HashSet<>();

    private Optional<Transaction> currentTransaction = Optional.empty();

    private final TransactionFactory transactionManager;
    private final CoinBank coinBank;
    private final ProductStorage productStorage;

    @Autowired
    public VendingMachine(TransactionFactory transactionManager, CoinBank coinBank, ProductStorage productStorage) {
        this.transactionManager = transactionManager;
        this.coinBank = coinBank;
        this.productStorage = productStorage;
    }

    public String getDisplay() {
        return "Insert coin";
    }

    public void insertCoin(Coin coin) {
        if (!currentTransaction.isPresent()) {
            currentTransaction = Optional.of(transactionManager.createTransaction());
        }

        currentTransaction.get().add(coin);
    }

    public void selectProduct(String productCode) {
        currentTransaction.ifPresent(t -> {
                    Money productPrice = productStorage.getProductPrice(productCode);

                    if (t.getValue().greaterOrEquals(productPrice) && productStorage.isProductAvailable(productCode)) {
                        t.reduce(productPrice);
                        productsTray.add(productStorage.getProduct(productCode));
                        coinReturnTray.addAll(coinBank.change(t));
                        currentTransaction = Optional.empty();
                    }
                }
        );
    }

    public void cancel() {
        currentTransaction.ifPresent(t -> {
            coinReturnTray.addAll(coinBank.change(t));
        });

        currentTransaction = Optional.empty();
    }

    public Set<Product> getProductsTray() {
        return productsTray;
    }

    public Set<Coin> getCoinReturnTray() {
        return coinReturnTray;
    }

    @VisibleForTesting
    public Optional<Transaction> getCurrentTransaction() {
        return currentTransaction;
    }
}
