package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.*;
import codepot.vendingmachine.infrastructure.notifiers.JiraServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.MailServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author kamil.essekkat on 2015-08-29.
 */
@Module(
        injects = {VendingMachine.class, Transaction.class, TransactionFactory.class, ProductStorage.class}
)
public class DaggerModule {
    @Provides
    Transaction provideTransaction() {
        return new Transaction();
    }

    @Singleton
    @Provides
    TransactionFactory provideTransactionFactory(Provider<Transaction> transactionProvider) {
        return new DaggerTransactionFactoryImpl(transactionProvider);
    }

    @Provides
    @Named("jiraServiceNotifier")
    ServiceNotifier provideJiraSN() {
        return new JiraServiceNotifier();
    }

    @Provides
    @Named("mailServiceNotifier")
    ServiceNotifier provideMailSN() {
        return new MailServiceNotifier();
    }

    @Provides
    List<ServiceNotifier> provideProductStorageSNs(@Named("mailServiceNotifier") ServiceNotifier s1, @Named("jiraServiceNotifier") ServiceNotifier s2) {
        return Lists.newArrayList(s1, s2);
    }

    @Provides
    ProductStorage provideProductStorage(List<ServiceNotifier> notifiers) {
        return new ProductStorage(notifiers);
    }

    @Singleton
    @Provides
    VendingMachine provideVendingMachine(TransactionFactory transactionFactory, ProductStorage productStorage) {
        return new VendingMachine(transactionFactory, productStorage);
    }

}
