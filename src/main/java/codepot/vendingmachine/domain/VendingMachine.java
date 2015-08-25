package codepot.vendingmachine.domain;

import com.google.common.annotations.VisibleForTesting;
import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import java.util.Optional;
import java.util.Set;

public class VendingMachine {

    private final TransactionFactory transactionFactory;
    private Optional<Transaction> currentTransaction = Optional.empty();

    public VendingMachine(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public String getDisplay() {
        return "INSERT A COIN";
    }

    public void selectProduct(String productCode) {
        currentTransaction = Optional.empty();
    }

    public void insertCoin(Coin coin) {
        if (!currentTransaction.isPresent()) {
            currentTransaction = Optional.of(transactionFactory.createTransaction());
        }

        currentTransaction.get().add(coin);
    }

    public void cancel() {
        currentTransaction = Optional.empty();
    }

    public Set<Coin> getCoinReturnTray() {
        return null;
    }

    public Set<Product> getProductsTray() {
        return null;
    }


    @VisibleForTesting
    public Optional<Transaction> getCurrentTransaction() {
        return currentTransaction;
    }

    public static class Builder {

        private MutablePicoContainer pico = new DefaultPicoContainer();

        public Builder() {
            pico.addComponent(TransactionFactory.class, new PicoContainerTransactionFactory(pico));
            pico.as(Characteristics.CACHE).addComponent(VendingMachine.class);

        }

        public VendingMachine build() {
            return pico.getComponent(VendingMachine.class);
        }
    }
}
