package codepot.vendingmachine.integration;

import codepot.vendingmachine.domain.Product;

public interface EmailService {
    void sendSupplyRequestToVendor(Product runningOutOf);
}
