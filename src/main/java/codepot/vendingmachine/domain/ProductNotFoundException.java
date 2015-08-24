package codepot.vendingmachine.domain;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String code) {
        super(String.format("Could not find product of given code: %s", code));
    }
}
