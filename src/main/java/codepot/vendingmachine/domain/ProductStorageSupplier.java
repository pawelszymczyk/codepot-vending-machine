package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;

public class ProductStorageSupplier {

    private final List<ServiceNotifier> serviceNotifierList;
    private ProductStorage productStorage;

    @Inject
    public ProductStorageSupplier(List<ServiceNotifier> serviceNotifierList) {
        this.serviceNotifierList = serviceNotifierList;
    }


    public void injectExternalProductStorage(Function<List<ServiceNotifier>, ProductStorage> productStorage) {
        this.productStorage = productStorage.apply(serviceNotifierList);
    }

    public ProductStorage getProductStorage() {
        if (productStorage == null) {
            productStorage = new DefaultProductStorage(serviceNotifierList);
        }

        return productStorage;
    }
}
