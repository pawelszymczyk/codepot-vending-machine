package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.VendingMachineModule;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import dagger.ObjectGraph;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class VendingMachine {

    private final Set<Product> productsTray = new HashSet<>();
    private final Set<Coin> coinReturnTray = new HashSet<>();

    private Optional<Transaction> currentTransaction = Optional.empty();

    private TransactionFactory transactionFactory;
    private CoinBank coinBank;
    private ProductStorage productStorage;

    @Inject
    public VendingMachine(TransactionFactory transactionFactory, ProductStorage productStorage, CoinBank coinBank) {
        this.transactionFactory = transactionFactory;
        this.coinBank = coinBank;
        this.productStorage = productStorage;
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

    public static class Builder {

        ObjectGraph objectGraph;
        List<Object> modules = Lists.newArrayList();
        private Function<List<ServiceNotifier>, ProductStorage> productStorage;

        public Builder withModule(Object module) {
            modules.add(module);
            return this;
        }

        public VendingMachine build() {
            modules.add(new VendingMachineModule(productStorage));
            objectGraph = ObjectGraph.create(modules.toArray());
            return objectGraph.get(VendingMachine.class);
        }

        public Builder withExternalProductStorage(Function<List<ServiceNotifier>, ProductStorage> productStorage) {
            this.productStorage = productStorage;

            return this;
        }
    }
}
