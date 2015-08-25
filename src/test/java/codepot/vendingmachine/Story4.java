package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.suncorp.SunCorpProductStorage;
import codepot.vendingmachine.infrastructure.suncorp.SunCorpProducts;
import org.junit.Test;

public class Story4 {

    @Test
    public void shouldFullyIntegrateWithSunCorpProductStorage() {
        //given
        VendingMachine vendingMachine = new VendingMachine.Builder()
                    .withExternalProductStorage(SunCorpProductStorage::new).build();

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(SunCorpProducts.CARROT.getCode());

        //then
        vendingMachine.getProductsTray().contains(SunCorpProducts.CARROT);
    }
}
