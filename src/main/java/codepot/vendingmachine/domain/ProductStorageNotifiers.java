package codepot.vendingmachine.domain;

import codepot.vendingmachine.infrastructure.notifiers.JiraServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.MailServiceNotifier;
import codepot.vendingmachine.infrastructure.notifiers.ServiceNotifier;
import com.google.common.collect.Lists;

import java.util.List;

public class ProductStorageNotifiers {

    private List<ServiceNotifier> serviceNotifiers;

    public ProductStorageNotifiers(JiraServiceNotifier jiraServiceNotifier, MailServiceNotifier mailServiceNotifier) {
        serviceNotifiers = Lists.newArrayList(jiraServiceNotifier, mailServiceNotifier);
    }

    public List<ServiceNotifier> getServiceNotifiers() {
        return serviceNotifiers;
    }
}
