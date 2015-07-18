package codepot.vendingmachine.infrastructure;

import codepot.vendingmachine.integration.OutOfMoneyNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsOutOfMoneyNotifier implements OutOfMoneyNotifier {

    private static final Logger logger = LoggerFactory.getLogger(SmsOutOfMoneyNotifier.class);

    @Override
    public void sendSupplyRequestToVendor() {
        logger.info("Out of money, sending sms to vendor");
    }
}
