package codepot.vendingmachine.domain;

import codepot.vendingmachine.integration.ServiceNotifier;

import java.util.Arrays;
import java.util.List;

public class ProductStorage {

    private final List<ServiceNotifier> serviceNotifiers;

    public ProductStorage(List<ServiceNotifier> serviceNotifiers) {
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
