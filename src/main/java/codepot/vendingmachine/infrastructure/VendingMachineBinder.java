package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.Hk2TransactionFactory;
import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.domain.TransactionFactory;
import codepot.vendingmachine.domain.VendingMachine;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class VendingMachineBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(Hk2TransactionFactory.class).in(Singleton.class).to(TransactionFactory.class);
        bind(Transaction.class).to(Transaction.class);
        bind(VendingMachine.class).in(Singleton.class).to(VendingMachine.class);
    }
}
