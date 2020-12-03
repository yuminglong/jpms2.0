package com.jiebao.jpms.util;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
