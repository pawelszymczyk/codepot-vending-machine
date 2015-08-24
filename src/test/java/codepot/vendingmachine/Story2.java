package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.DefaultProducts;
import codepot.vendingmachine.domain.Product;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.VendingMachineModule;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Story2 {

    private static final Coin[] coinBankCoins = new Coin[]{Coin.NICKEL, Coin.NICKEL, Coin.NICKEL};

    private static final List<ServiceNotifier> outOfProductNotifiers = Lists.newArrayList(mock(ServiceNotifier.class), mock(ServiceNotifier.class));

    private VendingMachine vendingMachine; //create vendingMachine with mock notifiers

    static class TestVendingMachineModule extends VendingMachineModule {
        @Override
        protected void configure() {
            super.configure();
        }

        @Provides
        @Singleton
        public List<ServiceNotifier> outOfProductsNotifier() {
            return outOfProductNotifiers;
        }
    }

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new TestVendingMachineModule());

        vendingMachine = injector.getInstance(VendingMachine.class);
    }

    @Test
    public void shouldPutSelectedProductOnTray() {
        //given
        Product chips = DefaultProducts.CHIPS;

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(chips.getCode());

        //then
        assertThat(vendingMachine.getProductsTray().size()).isEqualTo(1);
        assertThat(vendingMachine.getProductsTray().iterator().next().getCode()).isEqualTo(chips.getCode());
    }

    @Test
    public void shouldNotPutSelectedProductOnTrayWhenNotEnoughMoney() {
        //given
        DefaultProducts chips = DefaultProducts.CHIPS;

        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(chips.getCode());

        //then
        assertThat(vendingMachine.getProductsTray().isEmpty()).isTrue();
    }

    @Test
    public void shouldNotifyRecipientsAboutLackOfProduct() {
        //given
        DefaultProducts chips = DefaultProducts.CHIPS;

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(chips.getCode());

        //then
        outOfProductNotifiers.forEach(n -> verify(n).doNotify());
    }
}
