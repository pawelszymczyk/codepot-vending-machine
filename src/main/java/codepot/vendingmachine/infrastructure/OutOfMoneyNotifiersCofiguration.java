package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.integration.OutOfMoneyNotifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutOfMoneyNotifiersCofiguration {

    @Bean
    public OutOfMoneyNotifier mailOutOfMoneyNotifier() {
        return new MailOutOfMoneyNotifier();
    }

    @Bean
    public OutOfMoneyNotifier smsOutOfMoneyNotifier() {
        return new SmsOutOfMoneyNotifier();
    }
}
