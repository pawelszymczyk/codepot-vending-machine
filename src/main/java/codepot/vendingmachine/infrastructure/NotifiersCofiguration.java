package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.infrastructure.notifiers.JiraServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.MailServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.SmsServiceNotifier;
import codepot.vendingmachine.integration.ServiceNotifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotifiersCofiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServiceNotifier mailNotifier() {
        return new MailServiceNotifier();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceNotifier smsNotifier() {
        return new SmsServiceNotifier();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceNotifier jiraNotifier() {
        return new JiraServiceNotifier();
    }
}
