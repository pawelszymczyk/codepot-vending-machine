package codepot.vendingmachine.infrastructure.suncorp;

import codepot.vendingmachine.domain.Money;
import codepot.vendingmachine.domain.Product;
import codepot.vendingmachine.domain.ProductNotFoundException;
import codepot.vendingmachine.integration.ServiceNotifier;

import java.util.Arrays;
import java.util.List;

public class SunCorpProductStorage {

    private final List<ServiceNotifier> serviceNotifiers;

    public SunCorpProductStorage(List<ServiceNotifier> serviceNotifiers) {
        this.serviceNotifiers = serviceNotifiers;
    }

    public Money getProductPrice(String productCode) {
        return findProductByCode(productCode)
                .getPrice();
    }

    public boolean isProductAvailable(String productCode) {
        boolean isProductAvailable = isAviailable(productCode);

        if (!isProductAvailable) {
            serviceNotifiers.forEach(n -> n.doNotify());
        }

        return isProductAvailable;
    }

    private boolean isAviailable(String productCode) {
        return true;
    }

    public Product getProduct(String productCode) {
        return findProductByCode(productCode);
    }

    private Product findProductByCode(String productCode) {
        return Arrays.stream(Product.values()).filter(p -> p.getCode().equals(productCode))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException(productCode));
    }
}
