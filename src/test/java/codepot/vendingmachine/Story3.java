package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.CoinBank;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.VendingMachineModule;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Story3 {

    private static final Coin[] coinBankCoins = new Coin[]{Coin.NICKEL, Coin.NICKEL, Coin.NICKEL};

    private static final ServiceNotifier outOfMoneyNotifier = mock(ServiceNotifier.class);

    private VendingMachine vendingMachine;

    static class TestVendingMachineModule extends AbstractModule {
        @Override
        protected void configure() {

        }

        @Provides
        @Singleton
        public CoinBank coinBank() {
            return new CoinBank(outOfMoneyNotifier, coinBankCoins);
        }
    }

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new VendingMachineModule());

        vendingMachine = injector.getInstance(VendingMachine.class);
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
                .isEqualTo(new BigDecimal(0.10).setScale(2, RoundingMode.HALF_UP));
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
        Injector injector = Guice.createInjector(Modules.override(new VendingMachineModule()).with(new TestVendingMachineModule()));

        VendingMachine vendingMachine = injector.getInstance(VendingMachine.class);

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
