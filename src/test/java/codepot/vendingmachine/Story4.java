package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.suncorp.SunCorpProducts;
import org.junit.Test;

public class Story4 {

    @Test
    public void shouldFullyIntegrateWithSunCorpProductStorage() {
        //given
        VendingMachine vendingMachine = null;
        /*
            here integrate VendingMachine with SunCorpProductStorage, try to pick notifiers from container not create them by new eg:

            vendingMachine = new VendingMachine.Builder().withExternalProductStorage(
                    (List<ServiceNotifier> notifiers) -> {
                        new SunCorpProductStorage(notifiers);
                    }
            ).build();
        */

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(SunCorpProducts.CARROT.getCode());

        //then
        vendingMachine.getProductsTray().contains(SunCorpProducts.CARROT);
    }
}
