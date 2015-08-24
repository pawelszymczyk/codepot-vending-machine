package codepot.vendingmachine.domain;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class VendingMachine {

    private final Set<Coin> coinReturnTray = new HashSet<>();
    private final Set<Product> productsTray = new HashSet<>();

    private Optional<Transaction> currentTransaction = Optional.empty();

    private final TransactionFactory transactionFactory;
    private final CoinBank coinBank;
    private final ProductStorage productStorage;

    @Inject
    public VendingMachine(TransactionFactory transactionFactory, CoinBank coinBank, ProductStorage productStorage) {
        this.transactionFactory = transactionFactory;
        this.coinBank = coinBank;
        this.productStorage = productStorage;
    }

    public String getDisplay() {
        return "INSERT A COIN";
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

    public void insertCoin(Coin coin) {
        if (!currentTransaction.isPresent()) {
            currentTransaction = Optional.of(transactionFactory.createTransaction());
        }

        currentTransaction.get().add(coin);
    }

    public void cancel() {
        currentTransaction.ifPresent(t -> {
            coinReturnTray.addAll(coinBank.change(t));
        });

        currentTransaction = Optional.empty();
    }

    public Set<Coin> getCoinReturnTray() {
        return coinReturnTray;
    }

    public Set<Product> getProductsTray() {
        return null;
    }


    @VisibleForTesting
    public Optional<Transaction> getCurrentTransaction() {
        return currentTransaction;
    }
}
