package codepot.vendingmachine.infrastructure.notifiers;

import codepot.vendingmachine.integration.ServiceNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsServiceNotifier implements ServiceNotifier {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceNotifier.class);

    @Override
    public void doNotify() {
        logger.info("Out of money, sending sms to vendor");
    }
}
