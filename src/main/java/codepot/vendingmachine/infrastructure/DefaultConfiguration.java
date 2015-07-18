package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.ProductStorage;
import codepot.vendingmachine.integration.OutOfMoneyNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DefaultConfiguration {

    @Autowired
    List<OutOfMoneyNotifier> outOfMoneyNotifiers;

    @Bean
    @ConditionalOnMissingBean
    public CoinBank coinBank() {
        return new DefaultCoinBank(outOfMoneyNotifiers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ProductStorage productStorage() {
        return new DefaultProductStorage();
    }
}