package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.VendingMachineBinder;
import com.google.common.annotations.VisibleForTesting;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.internal.ServiceLocatorFactoryImpl;
import org.glassfish.hk2.utilities.Binder;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class VendingMachine {

    private final Set<Product> productsTray = new HashSet<>();
    private final Set<Coin> coinReturnTray = new HashSet<>();

    private Optional<Transaction> currentTransaction = Optional.empty();

    private TransactionFactory transactionFactory;

    @Inject
    public VendingMachine(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
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
        currentTransaction = Optional.empty();

    }

    public void cancel() {
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

        public Builder() {
            Binder[] b = new Binder[1];
            b[0] = new VendingMachineBinder();
//            locator = ServiceLocatorUtilities.bind("asd", b); - I don't want static container
            locator = createServiceLocator("asd", b);
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
            return locator.getService(VendingMachine.class);
        }
    }
}
