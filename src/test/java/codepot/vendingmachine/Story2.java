package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.DefaultProducts;
import codepot.vendingmachine.domain.Product;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;
import dagger.Module;
import dagger.Provides;
import org.junit.Test;

import javax.inject.Named;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Story2 {
    @Module(
            overrides = true,
            library = true
    )
    static class DaggerTestModule{

        @Provides
        List<ServiceNotifier> provideProductStorageSNs(){
            return outOfProductNotifiers;
        }
    }

    private static List<ServiceNotifier> outOfProductNotifiers = Lists.newArrayList(mock(ServiceNotifier.class), mock(ServiceNotifier.class));

    VendingMachine vendingMachine = new VendingMachine.Builder(new DaggerTestModule()).build(); //try to provide this object in some other way than new VendingMachine(...);

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
