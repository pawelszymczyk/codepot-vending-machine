package codepot.vendingmachine.infrastructure;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.behaviors.Caching;

import java.util.Properties;

public class LoggingAwareByDefault<T> extends Caching {

    @Override
    public <T> ComponentAdapter<T> createComponentAdapter(ComponentMonitor componentMonitor, LifecycleStrategy lifecycleStrategy, Properties componentProperties, Object componentKey, Class<T> componentImplementation, org.picocontainer.Parameter... parameters) throws PicoCompositionException {
        ComponentAdapter<T> ca =
                super.createComponentAdapter(componentMonitor,
                        lifecycleStrategy,
                        componentProperties,
                        componentKey,
                        componentImplementation, parameters);

        LoggingAware loggingAware = new LoggingAware(ca);

        return new LoggingAware(loggingAware);
    }

}
