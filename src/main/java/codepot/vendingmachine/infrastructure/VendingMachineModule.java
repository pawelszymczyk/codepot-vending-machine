package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.ProductStorage;
import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.domain.TransactionFactory;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.notifiers.JiraServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.MailServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.SmsServiceNotifier;
import com.google.common.collect.Lists;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;

@Module(
        injects = {VendingMachine.class, Transaction.class, TransactionFactory.class}
)
public class VendingMachineModule {

    @Provides
    Transaction transaction() {
        return new Transaction();
    }

    @Provides
    @Singleton
    TransactionFactory transactionFactory(Provider<Transaction> transactionProvider) {
        return new DaggerTransactionFactory(transactionProvider);
    }

    @Provides
    @Singleton
    @Named("jiraServiceNotifier")
    ServiceNotifier jiraServiceNotifier() {
        return new JiraServiceNotifier();
    }

    @Provides
    @Singleton
    @Named("mailServiceNotifier")
    ServiceNotifier mailServiceNotifier() {
        return new MailServiceNotifier();
    }

    @Provides
    @Singleton
    @Named("smsServiceNotifier")
    ServiceNotifier smsServiceNotifier() {
        return new SmsServiceNotifier();
    }

    @Provides
    @Singleton
    protected List<ServiceNotifier> productStorageServiceNotifiers(@Named("jiraServiceNotifier") ServiceNotifier jiraServiceNotifier,
                                                         @Named("mailServiceNotifier") ServiceNotifier mailServiceNotifier) {
        return Lists.newArrayList(jiraServiceNotifier, mailServiceNotifier);
    }

    @Provides
    @Singleton
    ProductStorage productStorage(List<ServiceNotifier> serviceNotifiers) {
        return new ProductStorage(serviceNotifiers);
    }

    @Provides
    @Singleton
    CoinBank coinBank(@Named("smsServiceNotifier") ServiceNotifier serviceNotifier) {
        return new CoinBank(serviceNotifier);
    }

    @Provides
    @Singleton
    VendingMachine vendingMachine(TransactionFactory transactionFactory, ProductStorage productStorage, CoinBank coinBank) {
        return new VendingMachine(transactionFactory, productStorage, coinBank);
    }
}
