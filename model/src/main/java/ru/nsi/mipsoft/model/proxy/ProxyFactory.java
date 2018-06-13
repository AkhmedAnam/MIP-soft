package ru.nsi.mipsoft.model.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public interface ProxyFactory {
    Proxy createProxy(ClassLoader classLoader, Class<?>[] interfaces, InvocationHandler invocationHandler);
}
