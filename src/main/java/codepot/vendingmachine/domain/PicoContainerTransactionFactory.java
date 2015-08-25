package codepot.vendingmachine.domain;

import org.picocontainer.Characteristics;
import org.picocontainer.MutablePicoContainer;

public class PicoContainerTransactionFactory implements TransactionFactory {

    private final MutablePicoContainer pico;

    public PicoContainerTransactionFactory(MutablePicoContainer pico) {
        this.pico = pico;
        pico.as(Characteristics.NO_CACHE).addComponent(Transaction.class);
    }

    @Override
    public Transaction createTransaction() {
        return pico.getComponent(Transaction.class);
    }
}
