package proxy;

import java.lang.reflect.Proxy;

public class Ioc {
    public static <T> T createProxy(T objectClass, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(),
                new Class<?>[]{interfaceType},
                new LoggingHandler(objectClass)
        );
    }
}
