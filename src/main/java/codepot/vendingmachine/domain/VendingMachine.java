package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.VendingMachineModule;
import com.google.common.annotations.VisibleForTesting;
import dagger.ObjectGraph;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class VendingMachine {

    private final Set<Product> productsTray = new HashSet<>();
    private final Set<Coin> coinReturnTray = new HashSet<>();

    private Optional<Transaction> currentTransaction = Optional.empty();

    private TransactionFactory transactionFactory;
//    private CoinBank coinBank;
//    private ProductStorage productStorage;

    @Inject
    public VendingMachine(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
//        this.coinBank = coinBank;
//        this.productStorage = productStorage;
    }

    public String getDisplay() {
        return "Insert coin";
    }

    public void insertCoin(Coin coin) {
        if (!currentTransaction.isPresent()) {
            currentTransaction = Optional.of(transactionFactory.createTransaction());
        }

        currentTransaction.get().add(coin);
    }

    public void selectProduct(String productCode) {
//        currentTransaction.ifPresent(t -> {
//                    Money productPrice = productStorage.getProductPrice(productCode);
//
//                    if (t.getValue().greaterOrEquals(productPrice) && productStorage.isProductAvailable(productCode)) {
//                        t.reduce(productPrice);
//                        productsTray.add(productStorage.getProduct(productCode));
//                        coinReturnTray.addAll(coinBank.change(t));
        currentTransaction = Optional.empty();
//                    }
//                }
//        );
    }

    public void cancel() {
//        currentTransaction.ifPresent(t -> {
//            coinReturnTray.addAll(coinBank.change(t));
//        });
//
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

    public static class Builder {

        ObjectGraph objectGraph;

        public Builder() {
            objectGraph = ObjectGraph.create(new VendingMachineModule());
        }

        public VendingMachine build() {
            return objectGraph.get(VendingMachine.class);
        }
    }
}
