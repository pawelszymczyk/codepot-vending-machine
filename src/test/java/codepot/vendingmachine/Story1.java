package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.DefaultProducts;
import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.domain.VendingMachine;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class Story1 {

    VendingMachine vendingMachine; //try to provide this object in some other way then new VendingMachine(...);

    @Test
    public void shouldCreateVendingMachineWithClosedTransaction() {
        //expect
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getCurrentTransaction()).isEmpty();
    }

    @Test
    public void shouldCreateNewTransactionAfterInsertFirstCoin() {
        //when
        vendingMachine.insertCoin(Coin.DIME);

        //then
        assertThat(vendingMachine.getCurrentTransaction()).isPresent();
    }

    @Test
    public void shouldContinueAlreadyExistedTransaction() {
        //expect
        assertThat(vendingMachine.getCurrentTransaction()).isEmpty();

        //given
        vendingMachine.insertCoin(Coin.DIME);
        Optional<Transaction> transaction = vendingMachine.getCurrentTransaction();

        //when
        vendingMachine.insertCoin(Coin.NICKEL);

        //then
        assertThat(vendingMachine.getCurrentTransaction()).isEqualTo(transaction);
    }

    @Test
    public void shouldEndTransactionAfterSelectCancelButton() {
        //given
        vendingMachine.insertCoin(Coin.DIME);

        //when
        vendingMachine.cancel();

        //then
        assertThat(vendingMachine.getCurrentTransaction()).isEmpty();
        assertThat(vendingMachine.getCoinReturnTray().size()).isEqualTo(1);
        assertThat(vendingMachine.getCoinReturnTray().contains(Coin.DIME));
    }

    @Test
    public void shouldEndTransactionAfterSelectProduct() {
        //given
        DefaultProducts chips = DefaultProducts.CHIPS;

        vendingMachine.insertCoin(Coin.QUARTER);
        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(chips.getCode());

        //then
        assertThat(vendingMachine.getCurrentTransaction()).isEmpty();
    }

    @Test
    public void shouldNotEndTransactionAfterSelectProductWithoutEnoughMoney() {
        //given
        DefaultProducts chips = DefaultProducts.CHIPS;

        vendingMachine.insertCoin(Coin.QUARTER);

        //when
        vendingMachine.selectProduct(chips.getCode());

        //then
        assertThat(vendingMachine.getCurrentTransaction()).isPresent();
    }
}
