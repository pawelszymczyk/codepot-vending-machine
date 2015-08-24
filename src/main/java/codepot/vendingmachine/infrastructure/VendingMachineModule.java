package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.GuiceTransactionFactory;
import codepot.vendingmachine.domain.TransactionFactory;
import codepot.vendingmachine.domain.VendingMachine;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class VendingMachineModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TransactionFactory.class).to(GuiceTransactionFactory.class).in(Singleton.class);
        bind(VendingMachine.class).in(Singleton.class);
    }
}
