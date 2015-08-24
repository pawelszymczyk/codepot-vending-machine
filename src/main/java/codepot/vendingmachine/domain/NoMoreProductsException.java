package codepot.vendingmachine.domain;

public class NoMoreProductsException extends RuntimeException {

    public NoMoreProductsException(String code) {
        super(String.format("No more products of given code: %s", code));
    }

}
