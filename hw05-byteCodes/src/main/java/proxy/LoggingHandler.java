package proxy;

import annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggingHandler implements InvocationHandler {
    private final Object objectClass;

    public LoggingHandler(Object objectClass) {
        this.objectClass = objectClass;
    }

    @Override
    @Log
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method realMethod = objectClass.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (realMethod.isAnnotationPresent(Log.class)) {
            System.out.printf("executed method: %s, params: %s%n", method.getName(), Arrays.toString(args));
        }
        return method.invoke(objectClass, args);
    }
}
