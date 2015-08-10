package codepot.vendingmachine.infrastructure.notifiers;

import codepot.vendingmachine.integration.ServiceNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailServiceNotifier implements ServiceNotifier {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceNotifier.class);

    @Override
    public void doNotify() {
        logger.info("Out of money, sending email to vendor");
    }
}
