package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.log.KeyLogger;
import codepot.vendingmachine.infrastructure.VendingMachineModule;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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

    @KeyLogger
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

    @KeyLogger
    public void insertCoin(Coin coin) {
        if (!currentTransaction.isPresent()) {
            currentTransaction = Optional.of(transactionFactory.createTransaction());
        }

        currentTransaction.get().add(coin);
    }

    @KeyLogger
    public void cancel() {
        currentTransaction.ifPresent(t -> {
            coinReturnTray.addAll(coinBank.change(t));
        });

        currentTransaction = Optional.empty();
    }

    @KeyLogger
    public Set<Coin> getCoinReturnTray() {
        return coinReturnTray;
    }

    @KeyLogger
    public Set<Product> getProductsTray() {
        return productsTray;
    }


    @VisibleForTesting
    public Optional<Transaction> getCurrentTransaction() {
        return currentTransaction;
    }

    public static class Builder {

        private Function<List<ServiceNotifier>, ProductStorage> productStorage;

        public Builder() {
        }

        public Builder withExternalProductStorage(Function<List<ServiceNotifier>, ProductStorage> productStorage) {
            this.productStorage = productStorage;
            return this;
        }

        public VendingMachine build() {
            VendingMachineModule module = productStorage != null ? new VendingMachineModule(productStorage) : new VendingMachineModule();

            Injector injector = Guice.createInjector(module);


            return injector.getInstance(VendingMachine.class);

        }
    }
}
