package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.ServiceLogger;
import codepot.vendingmachine.infrastructure.VendingMachineBinder;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.internal.ServiceLocatorFactoryImpl;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

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
    private ProductStorageSupplier productStorageSupplier;

    @Inject
    public VendingMachine(TransactionFactory transactionFactory, CoinBank coinBank, ProductStorageSupplier productStorageSupplier) {
        this.transactionFactory = transactionFactory;
        this.coinBank = coinBank;
        this.productStorageSupplier = productStorageSupplier;
    }

    public String getDisplay() {
        return "Insert coin";
    }

    @ServiceLogger
    public void insertCoin(Coin coin) {
        if (!currentTransaction.isPresent()) {
            currentTransaction = Optional.of(transactionFactory.createTransaction());
        }

        currentTransaction.get().add(coin);
    }

    @ServiceLogger
    public void selectProduct(String productCode) {
        currentTransaction.ifPresent(t -> {
                    Money productPrice = productStorageSupplier.getProductStorage().getProductPrice(productCode);

                    if (t.getValue().greaterOrEquals(productPrice) && productStorageSupplier.getProductStorage().isProductAvailable(productCode)) {
                        t.reduce(productPrice);
                        productsTray.add(productStorageSupplier.getProductStorage().getProduct(productCode));
                        coinReturnTray.addAll(coinBank.change(t));
                        currentTransaction = Optional.empty();
                    }
                }
        );
    }

    @ServiceLogger
    public void cancel() {
        currentTransaction.ifPresent(t -> {
            coinReturnTray.addAll(coinBank.change(t));
        });

        currentTransaction = Optional.empty();
    }

    @ServiceLogger
    public Set<Product> getProductsTray() {
        return productsTray;
    }

    @ServiceLogger
    public Set<Coin> getCoinReturnTray() {
        return coinReturnTray;
    }

    @VisibleForTesting
    public Optional<Transaction> getCurrentTransaction() {
        return currentTransaction;
    }

    public static class Builder {

        private ServiceLocator locator;
        private List<Binder> binders = Lists.newArrayList();
        private Function<List<ServiceNotifier>, ProductStorage> productStorage;
        public Builder() {
            binders.add(new VendingMachineBinder());
        }

        private ServiceLocator createServiceLocator(String name, Binder[] binders) {
            ServiceLocatorFactory factory = new ServiceLocatorFactoryImpl();

            ServiceLocator locator = factory.create(name);
            bind(locator, binders);

            return locator;
        }

        public void bind(ServiceLocator locator, Binder... binders) {
            DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
            DynamicConfiguration config = dcs.createDynamicConfiguration();

            for (Binder binder : binders) {
                binder.bind(config);
            }

            config.commit();
        }

        public VendingMachine build() {
            locator = createServiceLocator("asd", binders.toArray(new Binder[binders.size()]));

            if (productStorage != null) {
                ProductStorageSupplier productStorageSupplier = locator.getService(ProductStorageSupplier.class);

                productStorageSupplier.injectExternalProductStorage(productStorage);
            }



            return locator.getService(VendingMachine.class);
        }

        public <T> Builder withSingletonBinding(T instance, TypeLiteral<?> contract)  {
            final int rankHigherThanDefault = 10;
            binders.add(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(instance).to(contract).named(contract.getType().getTypeName()).ranked(rankHigherThanDefault);
                }
            });

            return this;
        }

        public <T> Builder withSingletonBinding(T instance, Class<T> clazz, String name) {
            final int rankHigherThanDefault = 10;
            binders.add(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(instance).to(clazz).named(name).ranked(rankHigherThanDefault);
                }
            });
            return this;
        }

        public Builder withExternalProductStorage(Function<List<ServiceNotifier>, ProductStorage> productStorage) {
            this.productStorage = productStorage;

            return this;
        }
    }
}
