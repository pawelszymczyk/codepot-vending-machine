package codepot.vendingmachine.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TransactionManager {

    @Autowired
    private ApplicationContext applicationContext;

    Transaction getTransaction() {
        return applicationContext.getBean(Transaction.class);
    }
}
