package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.DefaultProducts;
import codepot.vendingmachine.domain.Product;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;
import org.glassfish.hk2.api.TypeLiteral;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Story2 {

    private Collection<ServiceNotifier> outOfProductNotifiers = Lists.newArrayList(mock(ServiceNotifier.class), mock(ServiceNotifier.class));

    private VendingMachine vendingMachine;

    @Before
    public void setUp() throws Exception {
        vendingMachine = new VendingMachine.Builder()
                .withSingletonBinding(outOfProductNotifiers, new TypeLiteral<List<ServiceNotifier>>(){})
                .build();
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
