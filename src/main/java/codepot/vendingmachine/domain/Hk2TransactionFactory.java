package codepot.vendingmachine.domain;

import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;

public class Hk2TransactionFactory implements TransactionFactory {

    private ServiceLocator serviceLocator;

    @Inject
    public Hk2TransactionFactory(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public Transaction createTransaction() {
        return serviceLocator.getService(Transaction.class);
    }
}
