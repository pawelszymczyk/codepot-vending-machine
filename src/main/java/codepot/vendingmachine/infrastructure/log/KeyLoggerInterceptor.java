package codepot.vendingmachine.infrastructure.log;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class KeyLoggerInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(KeyLoggerInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        logger.debug("Method: {}, invocation arguments: {}", invocation.getMethod().toString(), Arrays.toString(invocation.getArguments()));

        return invocation.proceed();
    }
}
