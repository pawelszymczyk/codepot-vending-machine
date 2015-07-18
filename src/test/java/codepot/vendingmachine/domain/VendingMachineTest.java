package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class VendingMachineTest {

    @Autowired
    private VendingMachine vendingMachine;

    @Test
    public void shouldCreateNewTransactionAfterInsertFirstCoin() {
        //expect
        assertThat(vendingMachine.getCurrentTransaction()).isEmpty();

        //when
        vendingMachine.insertCoin(Coin.DIME);

        //then
        assertThat(vendingMachine.getCurrentTransaction()).isPresent();
    }

    @Test
    public void shouldContinueOpenedTransaction() {
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
}