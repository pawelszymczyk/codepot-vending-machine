package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.domain.Money;
import codepot.vendingmachine.domain.Product;
import codepot.vendingmachine.domain.ProductNotFoundException;
import codepot.vendingmachine.domain.ProductStorage;

import java.util.Arrays;

public class DefaultProductStorage implements ProductStorage {

    @Override
    public Money getProductPrice(String productCode) {
        return findProductByCode(productCode)
                .getPrice();
    }

    @Override
    public boolean isProductAvailable(String productCode) {
        return true;
    }

    @Override
    public Product getProduct(String productCode) {
        return findProductByCode(productCode);
    }

    private Product findProductByCode(String productCode) {
        return Arrays.stream(Product.values()).filter(p -> p.getCode().equals(productCode))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException(productCode));
    }
}
