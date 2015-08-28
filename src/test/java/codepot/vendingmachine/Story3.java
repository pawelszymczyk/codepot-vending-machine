package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Sets;
import dagger.Module;
import dagger.Provides;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Named;
import javax.inject.Singleton;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Story3 {

    private static final Coin[] coinBankCoins = new Coin[]{Coin.NICKEL, Coin.NICKEL, Coin.NICKEL};

    private static final ServiceNotifier outOfMoneyNotifier = mock(ServiceNotifier.class);

    private VendingMachine vendingMachine;

    @Module(
            overrides = true,
            library = true
    )
    public class VendingMachineTestModule {

        @Provides
        @Singleton
        @Named("smsServiceNotifier")
        ServiceNotifier smsServiceNotifier() {
            return outOfMoneyNotifier;
        }

        @Provides
        @Singleton
        CoinBank coinBank(@Named("smsServiceNotifier") ServiceNotifier serviceNotifier) {
            return new CoinBank(serviceNotifier, coinBankCoins);
        }
    }

    @Before
    public void setUp() throws Exception {
        vendingMachine = new VendingMachine.Builder().build();
    }

    @Test
    public void shouldReturnChange() {
        //given
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct("A3");

        assertThat(vendingMachine.getCoinReturnTray().stream()
                    .map(c -> c.getValue())
                    .reduce((v1, v2) -> v1.add(v2))
                    .get())
                .isEqualTo(new BigDecimal(0.10).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void shouldReturnAllMoneyWhenCancelTransaction() {
        //given
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.DIME);
        vendingMachine.insertCoin(Coin.NICKEL);

        //when
        vendingMachine.cancel();

        assertThat(vendingMachine.getCoinReturnTray())
                .contains(Coin.QUARTER)
                .contains(Coin.DIME)
                .contains(Coin.NICKEL);
    }

    @Test
    public void shouldNotifyRecipientAboutLackOfMoney() {
        //given
        VendingMachine vendingMachine = new VendingMachine.Builder().withModule(new VendingMachineTestModule()).build();
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct("A3");

        //then all money from coins bank is on return tray
        vendingMachine.getCoinReturnTray().equals(Sets.newHashSet(coinBankCoins));

        //then
        verify(outOfMoneyNotifier).doNotify();
    }
}
