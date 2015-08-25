package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.notifiers.SmsServiceNotifier;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Story3 {

    private static final Coin[] coinBankCoins = new Coin[]{Coin.NICKEL, Coin.NICKEL, Coin.NICKEL};

    private SmsServiceNotifier outOfMoneyNotifier = mock(SmsServiceNotifier.class);

    private VendingMachine vendingMachine; //create vendingMachine with mock notifiers

    @Before
    public void setUp() throws Exception {
        MutablePicoContainer pico = new DefaultPicoContainer();

        pico.as(Characteristics.CACHE).addComponent(outOfMoneyNotifier);

        vendingMachine = new VendingMachine.Builder(Optional.of(pico)).build();
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
        MutablePicoContainer pico = new DefaultPicoContainer();

        pico.as(Characteristics.CACHE).addComponent(CoinBank.class, new CoinBank(outOfMoneyNotifier, coinBankCoins));

        VendingMachine vendingMachine = new VendingMachine.Builder(Optional.of(pico)).build();

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
