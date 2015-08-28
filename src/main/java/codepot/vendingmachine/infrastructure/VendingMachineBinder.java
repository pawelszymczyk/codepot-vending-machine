package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.Hk2TransactionFactory;
import codepot.vendingmachine.domain.ProductStorage;
import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.domain.TransactionFactory;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.notifiers.JiraServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.MailServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.SmsServiceNotifier;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import java.util.List;

public class VendingMachineBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(Transaction.class).to(Transaction.class);
        bind(Hk2TransactionFactory.class).in(Singleton.class).to(TransactionFactory.class);
        bind(JiraServiceNotifier.class).in(Singleton.class).to(ServiceNotifier.class).named("jiraServiceNotifier");
        bind(MailServiceNotifier.class).in(Singleton.class).to(ServiceNotifier.class).named("mailServiceNotifier");
        bind(SmsServiceNotifier.class).in(Singleton.class).to(ServiceNotifier.class).named("smsServiceNotifier");
        bindFactory(ProductStorageNotifiersFactory.class).in(Singleton.class).to(new TypeLiteral<List<ServiceNotifier>>() {});
        bind(ProductStorage.class).in(Singleton.class).to(ProductStorage.class);
        bind(CoinBank.class).in(Singleton.class).to(CoinBank.class);
        bind(VendingMachine.class).in(Singleton.class).to(VendingMachine.class);
    }
}
