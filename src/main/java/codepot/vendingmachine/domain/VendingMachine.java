package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.SpringTransactionFactory;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class VendingMachine {

    public final Set<Product> productTray = new HashSet<>();
    public final Set<Coin> coinReturnTray = new HashSet<>();

    private Optional<Transaction> currentTransaction = Optional.empty();

    private final SpringTransactionFactory transactionManager;
    private final CoinBank coinBank;
    private final ProductStorage productStorage;

    @Autowired
    public VendingMachine(SpringTransactionFactory transactionManager, CoinBank coinBank, ProductStorage productStorage) {
        this.transactionManager = transactionManager;
        this.coinBank = coinBank;
        this.productStorage = productStorage;
    }

    public String getDisplay() {
        return "Insert coin";
    }

    public void insertCoin(Coin coin) {
        if (!currentTransaction.isPresent()) {
            currentTransaction = Optional.of(transactionManager.createTransaction());
        }

        currentTransaction.get().add(coin);
    }

    public void selectProduct(String productCode) {
        currentTransaction.ifPresent(t -> {
                    Money productPrice = productStorage.getProductPrice(productCode);

                    if (t.getValue().greaterOrEquals(productPrice) && productStorage.isProductAvailable(productCode)) {
                        t.reduce(productPrice);
                        productTray.add(productStorage.getProduct(productCode));
                        coinReturnTray.addAll(coinBank.change(t));
                    }
                }
        );
    }

    @VisibleForTesting
    Optional<Transaction> getCurrentTransaction() {
        return currentTransaction;
    }
}
