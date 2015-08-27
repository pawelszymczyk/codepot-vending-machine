package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.VendingMachineBinder;
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

public class VendingMachine {

    private final Set<Product> productsTray = new HashSet<>();
    private final Set<Coin> coinReturnTray = new HashSet<>();

    private Optional<Transaction> currentTransaction = Optional.empty();

    private TransactionFactory transactionFactory;
    private CoinBank coinBank;
    private ProductStorage productStorage;

    @Inject
    public VendingMachine(TransactionFactory transactionFactory, CoinBank coinBank, ProductStorage productStorage) {
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

        private ServiceLocator locator;
        private List<Binder> binders = Lists.newArrayList();

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
    }
}
