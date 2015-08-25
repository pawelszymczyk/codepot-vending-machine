package codepot.vendingmachine.infrastructure.suncorp;

import codepot.vendingmachine.domain.Money;
import codepot.vendingmachine.domain.Product;
import codepot.vendingmachine.domain.ProductNotFoundException;
import codepot.vendingmachine.domain.ProductStorage;
import codepot.vendingmachine.infrastructure.notifiers.JiraServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.MailServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;

public class SunCorpProductStorage implements ProductStorage {

    private final List<ServiceNotifier> serviceNotifiers;

    public SunCorpProductStorage(List<ServiceNotifier> serviceNotifiers) {
        this.serviceNotifiers = serviceNotifiers;

        Preconditions.checkArgument(serviceNotifiers.size() == 2);
        Preconditions.checkArgument(serviceNotifiers.stream().filter(n -> n instanceof JiraServiceNotifier).findAny() != null);
        Preconditions.checkArgument(serviceNotifiers.stream().filter(n -> n instanceof MailServiceNotifier).findAny() != null);
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
        return Arrays.stream(SunCorpProducts.values()).filter(p -> p.getCode().equals(productCode))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException(productCode));
    }
}
