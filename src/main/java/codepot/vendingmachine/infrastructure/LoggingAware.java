package codepot.vendingmachine.infrastructure;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.HiddenImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggingAware<T> extends HiddenImplementation {

    Logger logger = LoggerFactory.getLogger(LoggingAware.class);

    public LoggingAware(ComponentAdapter delegate) {
        super(delegate);
    }

    protected Object invokeMethod(Object componentInstance, Method method, Object[] args, PicoContainer container)
            throws Throwable {

        logger.debug("Execute method: " + method.toString() + " args: " + Arrays.toString(args));

            Object object = method.invoke(componentInstance, args);
            return object;

    }
}
