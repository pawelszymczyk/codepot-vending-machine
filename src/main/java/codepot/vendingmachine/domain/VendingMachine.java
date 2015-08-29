package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.DaggerModule;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import dagger.ObjectGraph;

import javax.inject.Inject;
import java.util.*;

public class VendingMachine {

    private final TransactionFactory transactionFactory;
    private final ProductStorage productStorage;
    private final Set<Product> productsTray = new HashSet<>();
    private final Set<Coin> coinReturnTray = new HashSet<>();
    private Optional<Transaction> currentTransaction = Optional.empty();
//    private CoinBank coinBank;


    @Inject
    public VendingMachine(TransactionFactory transactionFactory, ProductStorage productStorage) {
        this.transactionFactory = transactionFactory;
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
//                        coinReturnTray.addAll(coinBank.change(t));
                        currentTransaction = Optional.empty();
                    }
                }
        );
    }


    public void insertCoin(Coin coin) {
        if (!currentTransaction.isPresent()) {
            this.currentTransaction = Optional.of(transactionFactory.createTransaction());
        }
    }

    public void cancel() {
        this.currentTransaction = Optional.empty();

    }

    public Set<Coin> getCoinReturnTray() {
        return this.coinReturnTray;
    }

    public Set<Product> getProductsTray() {
        return this.productsTray;
    }


    @VisibleForTesting
    public Optional<Transaction> getCurrentTransaction() {
        return currentTransaction;
    }

    public static class Builder {

        private final List<Object> modules;

        public Builder(Object... daggerTestModules) {
            this.modules = Lists.newArrayList(daggerTestModules);
            this.modules.add(new DaggerModule());
        }

        public VendingMachine build() {
            ObjectGraph objectGraph = ObjectGraph.create(this.modules);
            return objectGraph.get(VendingMachine.class);
        }
    }
}
