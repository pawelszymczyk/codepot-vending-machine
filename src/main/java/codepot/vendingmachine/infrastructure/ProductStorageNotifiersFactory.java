package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;
import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class ProductStorageNotifiersFactory implements Factory<List<ServiceNotifier>> {

    private final ServiceNotifier jiraServiceNotifier;
    private final ServiceNotifier mailServiceNotifier;

    @Inject
    public ProductStorageNotifiersFactory(@Named("jiraServiceNotifier") ServiceNotifier jiraServiceNotifier,
                                          @Named("mailServiceNotifier") ServiceNotifier mailServiceNotifier) {
        this.jiraServiceNotifier = jiraServiceNotifier;
        this.mailServiceNotifier = mailServiceNotifier;
    }

    @Override
    public List<ServiceNotifier> provide() {
        return Lists.newArrayList(jiraServiceNotifier, mailServiceNotifier);
    }

    @Override
    public void dispose(List<ServiceNotifier> instance) {

    }
}
