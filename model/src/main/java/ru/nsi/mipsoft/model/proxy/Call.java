package ru.nsi.mipsoft.model.proxy;

import java.lang.reflect.Method;

public class Call {
    private Method method;
    private Object[] args;
    private Object implementation;

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getImplementation() {
        return implementation;
    }

    public Call(Method method, Object[] args, Object implementation) {
        this.method = method;
        this.args = args;

        this.implementation = implementation;
    }
}
