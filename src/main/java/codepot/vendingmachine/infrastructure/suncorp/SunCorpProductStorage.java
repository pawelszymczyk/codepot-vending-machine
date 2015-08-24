package codepot.vendingmachine.infrastructure.suncorp;

import codepot.vendingmachine.domain.DefaultProducts;
import codepot.vendingmachine.domain.Money;
import codepot.vendingmachine.domain.ProductNotFoundException;
import codepot.vendingmachine.infrastructure.notifiers.JiraServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.MailServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;

public class SunCorpProductStorage {

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

    public DefaultProducts getProduct(String productCode) {
        return findProductByCode(productCode);
    }

    private DefaultProducts findProductByCode(String productCode) {
        return Arrays.stream(DefaultProducts.values()).filter(p -> p.getCode().equals(productCode))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException(productCode));
    }
}
