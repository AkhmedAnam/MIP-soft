package ru.nsi.mipsoft.proxy;

public interface ProxyExecutor {
    void schedule(Call call, boolean onlyIfIdle);
}
