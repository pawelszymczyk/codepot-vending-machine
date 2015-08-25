package codepot.vendingmachine.domain;

public interface ProductStorage {

    Money getProductPrice(String productCode);

    boolean isProductAvailable(String productCode);

    Product getProduct(String productCode);

}