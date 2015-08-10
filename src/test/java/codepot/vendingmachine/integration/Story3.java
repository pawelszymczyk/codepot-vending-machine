package codepot.vendingmachine.integration;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.suncorp.SunCorpProducts;
import org.junit.Test;

public class Story3 {


    @Test
    public void shouldFullyIntegrateWithSunCorpProductStorage() {
        //given
        VendingMachine vendingMachine = null;
        /*
            here integrate VendingMachine with SunCorpProductStorage, eg:

            vendingMachine = new VendingMachine.Builder().withExternalProductStorage(
                    (JiraServiceNotifier jiraServiceNotifier, MailServiceNotifier mailServiceNotifier) -> {
                        new SunCorpProductStorage(Lists.newArrayList(jiraServiceNotifier, mailServiceNotifier));
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
