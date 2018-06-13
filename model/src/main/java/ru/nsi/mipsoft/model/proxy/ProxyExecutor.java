package ru.nsi.mipsoft.model.proxy;

public interface ProxyExecutor {
    void schedule(Call call, boolean onlyIfIdle);
}
