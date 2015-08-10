package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.domain.TransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringTransactionFactory implements TransactionFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Transaction createTransaction() {
        return applicationContext.getBean(Transaction.class);
    }
}
