package codepot.vendingmachine.integration;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.VendingMachine;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OutOfMoneyNotifierTest.TestConfig.class)
public class OutOfMoneyNotifierTest {

    @Autowired
    private VendingMachine vendingMachine;

    @Autowired
    private Collection<OutOfMoneyNotifier> outOfMoneyNotifiers;

    @Test
    public void shouldNotifyAllReceipentsAboutLackOfMoney() {
        //given

        //candy = 0.65
        HashSet<Coin> coinBank = Sets.newHashSet(Coin.NICKEL, Coin.NICKEL, Coin.NICKEL);
        vendingMachine.initializeCoinBank(coinBank);

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct("A3");

        //then all money from coins bank is on return tray
        vendingMachine.getCoinReturnTray().equals(coinBank);

        //then
        outOfMoneyNotifiers.forEach(n -> verify(n).sendSupplyRequestToVendor());
    }

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan("codepot.vendingmachine")
    static class TestConfig {

        @Bean
        OutOfMoneyNotifier outOfMoneyNotifier1() {
            return mock(OutOfMoneyNotifier.class);
        }

        @Bean
        OutOfMoneyNotifier outOfMoneyNotifier2() {
            return mock(OutOfMoneyNotifier.class);
        }
    }
}
