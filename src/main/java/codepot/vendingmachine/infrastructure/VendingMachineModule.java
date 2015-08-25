package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.GuiceTransactionFactory;
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

public class VendingMachineModule extends AbstractModule {
    @Override
    protected void configure() {

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
