package codepot.vendingmachine.infrastructure;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class LoggingInterceptor implements MethodInterceptor{

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        logger.debug("Invoking method: " + invocation.getMethod().getName() + ", args: " + Arrays.toString(invocation.getArguments()));

        return invocation.proceed();
    }
}
