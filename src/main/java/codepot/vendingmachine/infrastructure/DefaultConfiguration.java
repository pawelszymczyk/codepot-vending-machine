package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.ProductStorage;
import codepot.vendingmachine.integration.ServiceNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DefaultConfiguration {

    @Autowired
    List<ServiceNotifier> notifiers;

    @Autowired
    @Qualifier("jiraNotifier")
    ServiceNotifier serviceNotifier;

    @Bean
    @ConditionalOnMissingBean
    public CoinBank coinBank() {
        return new CoinBank(serviceNotifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public ProductStorage productStorage() {
        return new ProductStorage(notifiers);
    }
}