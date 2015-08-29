package codepot.vendingmachine.domain;

public interface TransactionFactory {

    /**
     * Always return new Transaction, don't protect against creating few transactions simultaneously
     */
    Transaction createTransaction();
}
