package codepot.vendingmachine.domain;

import java.util.Arrays;

public class ProductStorage {

    public Money getProductPrice(String productCode) {
        return findProductByCode(productCode)
                .getPrice();
    }

    public boolean isProductAvailable(String productCode) {
        boolean isProductAvailable = isAviailable(productCode);

        return isProductAvailable;
    }

    private boolean isAviailable(String productCode) {
        return true;
    }

    public DefaultProducts getProduct(String productCode) {
        return findProductByCode(productCode);
    }

    private DefaultProducts findProductByCode(String productCode) {
        return Arrays.stream(DefaultProducts.values()).filter(p -> p.getCode().equals(productCode))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException(productCode));
    }
}