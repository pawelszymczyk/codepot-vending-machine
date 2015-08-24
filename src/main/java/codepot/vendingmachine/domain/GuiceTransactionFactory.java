package codepot.vendingmachine.domain;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class GuiceTransactionFactory implements TransactionFactory {

    private final Provider<Transaction> transactionProvider;

    @Inject
    public GuiceTransactionFactory(Provider<Transaction> transactionProvider) {
        this.transactionProvider = transactionProvider;
    }

    @Override
    public Transaction createTransaction() {
        return transactionProvider.get();
    }
}
