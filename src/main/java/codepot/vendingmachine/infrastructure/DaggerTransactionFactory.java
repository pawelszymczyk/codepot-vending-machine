package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.domain.TransactionFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class DaggerTransactionFactory implements TransactionFactory {

    private Provider<Transaction> transactionProvider;

    @Inject
    public DaggerTransactionFactory(Provider<Transaction> transactionProvider) {
        this.transactionProvider = transactionProvider;
    }

    @Override
    public Transaction createTransaction() {
        return transactionProvider.get();
    }
}
