package codepot.vendingmachine.integration;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.Product;
import codepot.vendingmachine.domain.VendingMachine;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServiceNotifierTest.TestConfig.class)
public class ServiceNotifierTest {

    private static final Coin[] coinBankCoins = new Coin[]{Coin.NICKEL, Coin.NICKEL, Coin.NICKEL};

    @Autowired
    private VendingMachine vendingMachine;

    @Autowired
    private Collection<ServiceNotifier> outOfMoneyNotifiers;

    @Autowired
    @Qualifier("jiraNotifier")
    private ServiceNotifier productServiceNotifier;

    @Test
    public void shouldNotifyAllRecipientsAboutLackOfMoney() {
        //given
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct("A3");

        //then all money from coins bank is on return tray
        vendingMachine.coinReturnTray.equals(Sets.newHashSet(coinBankCoins));

        //then
        outOfMoneyNotifiers.forEach(n -> verify(n).doNotify());
    }

    @Test
    public void shouldNotifyChoosenNotifierAboutLackOfProduct() {
        //given
        Product chips = Product.CHIPS;

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(chips.getCode());

        //then
        verify(productServiceNotifier).doNotify();
    }

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan("codepot.vendingmachine")
    static class TestConfig {

        @Autowired
        List<ServiceNotifier> notifiers;

        @Bean
        ServiceNotifier mailNotifier() {
            return mock(ServiceNotifier.class);
        }

        @Bean
        ServiceNotifier smsNotifier() {
            return mock(ServiceNotifier.class);
        }

        @Bean
        ServiceNotifier jiraNotifier() {
            return mock(ServiceNotifier.class);
        }

        @Bean
        CoinBank coinBank() {
            return new CoinBank(notifiers, coinBankCoins);
        }
    }
}
