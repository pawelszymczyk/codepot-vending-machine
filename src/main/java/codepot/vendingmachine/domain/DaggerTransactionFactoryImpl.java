package codepot.vendingmachine.domain;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author kamil.essekkat on 2015-08-29.
 */
public class DaggerTransactionFactoryImpl implements TransactionFactory {
    private Provider<Transaction> transactionProvider;

    @Inject
    public DaggerTransactionFactoryImpl(Provider<Transaction> transactionProvider) {
        this.transactionProvider = transactionProvider;
    }

    @Override
    public Transaction createTransaction() {
        return transactionProvider.get();
    }
}
