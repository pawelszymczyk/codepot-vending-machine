package codepot.vendingmachine.domain;

public interface Product {

    Money getPrice();

    String getCode();
}
