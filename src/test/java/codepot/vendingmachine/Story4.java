package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import codepot.vendingmachine.infrastructure.suncorp.SunCorpProductStorage;
import codepot.vendingmachine.infrastructure.suncorp.SunCorpProducts;
import org.junit.Test;

import java.util.List;

public class Story4 {

    @Test
    public void shouldFullyIntegrateWithSunCorpProductStorage() {
        //given
        VendingMachine vendingMachine = new VendingMachine.Builder().withExternalProductStorage(
                (List<ServiceNotifier> notifiers) -> new SunCorpProductStorage(notifiers)
        ).build();

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(SunCorpProducts.CARROT.getCode());

        //then
        vendingMachine.getProductsTray().contains(SunCorpProducts.CARROT);
    }
}
