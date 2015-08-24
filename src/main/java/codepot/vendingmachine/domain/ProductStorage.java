package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStorage {

    private final List<ServiceNotifier> serviceNotifiers;

    private Map<String, Product> productsStorage = new HashMap<>();
    private Map<String, Integer> productsQuantity = new HashMap<>();

    @Inject
    public ProductStorage(List<ServiceNotifier> serviceNotifiers) {
        this(serviceNotifiers,
                Lists.newArrayList(DefaultProducts.CANDY, DefaultProducts.CHIPS, DefaultProducts.COLA));
    }

    public ProductStorage(List<ServiceNotifier> serviceNotifiers, List<Product> products) {
        this.serviceNotifiers = serviceNotifiers;
        products.stream().forEach((p) -> {
            productsQuantity.put(p.getCode(), productsQuantity.getOrDefault(p.getCode(), 0) + 1);
            productsStorage.put(p.getCode(), p);
        });
    }

    public Money getProductPrice(String productCode) {
        return findProductByCode(productCode)
                .getPrice();
    }

    public boolean isProductAvailable(String productCode) {
        return isAviailable(productCode);

    }

    private boolean isAviailable(String productCode) {
        return productsQuantity.get(productCode) > 0;
    }

    public Product getProduct(String productCode) {
        if (!isProductAvailable(productCode)) {
            throw new NoMoreProductsException(productCode);
        }

        Product product = findProductByCode(productCode);
        productsQuantity.put(productCode, productsQuantity.get(productCode) - 1);


        if (!isProductAvailable(productCode)) {
            serviceNotifiers.forEach(n -> n.doNotify());
        }

        return product;
    }

    private Product findProductByCode(String productCode) {
        if (!productsStorage.containsKey(productCode)) throw new ProductNotFoundException(productCode);

        return productsStorage.get(productCode);
    }
}