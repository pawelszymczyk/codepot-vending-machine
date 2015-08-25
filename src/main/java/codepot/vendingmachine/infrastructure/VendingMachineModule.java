package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.DefaultProductStorage;
import codepot.vendingmachine.domain.GuiceTransactionFactory;
import codepot.vendingmachine.domain.ProductStorage;
import codepot.vendingmachine.domain.TransactionFactory;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.notifiers.JiraServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.SmsServiceNotifier;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class VendingMachineModule extends AbstractModule {
    private final Optional<Function<List<ServiceNotifier>, ProductStorage>> productStorage;

    public VendingMachineModule() {
        this.productStorage = Optional.empty();
    }

    public VendingMachineModule(Function<List<ServiceNotifier>, ProductStorage> productStorage) {
        this.productStorage = Optional.of(productStorage);
    }

    @Override
    protected void configure() {
        if (productStorage.isPresent()) {
            bind(ProductStorage.class).toInstance(productStorage.get().apply(Lists.newArrayList(outOfMoneyNotifier(), outOfMoneyNotifier())));
        } else {
            bind(ProductStorage.class).to(DefaultProductStorage.class).in(Singleton.class);
        }

        bind(TransactionFactory.class).to(GuiceTransactionFactory.class).in(Singleton.class);
        bind(VendingMachine.class).in(Singleton.class);

    }

    @Provides
    @Singleton
    public List<ServiceNotifier> outOfProductsNotifier() {
        return Lists.newArrayList(new SmsServiceNotifier(), new JiraServiceNotifier());
    }

    @Provides
    @Singleton
    public ServiceNotifier outOfMoneyNotifier() {
        return new SmsServiceNotifier();
    }
}
