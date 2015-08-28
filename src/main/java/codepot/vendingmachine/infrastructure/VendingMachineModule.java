package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.domain.TransactionFactory;
import codepot.vendingmachine.domain.VendingMachine;
import dagger.Module;
import dagger.Provides;

import javax.inject.Provider;
import javax.inject.Singleton;

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
    VendingMachine vendingMachine(TransactionFactory transactionFactory) {
        return new VendingMachine(transactionFactory);
    }
}
