package codepot.vendingmachine;

import codepot.vendingmachine.domain.Coin;
import codepot.vendingmachine.domain.DefaultProducts;
import codepot.vendingmachine.domain.Transaction;
import codepot.vendingmachine.domain.VendingMachine;
import codepot.vendingmachine.infrastructure.VendingMachineModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class Story1 {

    VendingMachine vendingMachine; //try to provide this object in some other way than new VendingMachine(...);

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new VendingMachineModule());

        vendingMachine = injector.getInstance(VendingMachine.class);
    }

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
    public void shouldCreateNewTransactionEachTime() {
        //given
        vendingMachine.insertCoin(Coin.DIME);
        Transaction firstTransaction = vendingMachine.getCurrentTransaction().get();
        vendingMachine.cancel();

        //when
        vendingMachine.insertCoin(Coin.DIME);

        //then
        assertThat(firstTransaction).isNotEqualTo(vendingMachine.getCurrentTransaction().get());
    }
}
