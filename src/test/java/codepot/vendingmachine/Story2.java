package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.DefaultProducts;
import codepot.vendingmachine.domain.Product;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;
import dagger.Module;
import dagger.Provides;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Singleton;
import java.util.List;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Story2 {

    private List<ServiceNotifier> outOfProductNotifiers = Lists.newArrayList(mock(ServiceNotifier.class), mock(ServiceNotifier.class));

    private VendingMachine vendingMachine;

    @Module(
            overrides = true,
            library = true
    )
    public class VendingMachineTestModule {

        @Provides
        @Singleton
        protected List<ServiceNotifier> productStorageServiceNotifiers() {
            return outOfProductNotifiers;
        }
    }

    @Before
    public void setUp() throws Exception {
        vendingMachine = new VendingMachine.Builder().withModule(new VendingMachineTestModule()).build();
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
