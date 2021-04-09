package com.company.ingestion.service.cash;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Cache<T, R> {

    private ConcurrentHashMap<T, R> values;
    private final Supplier<R> function;

    public Cache(Supplier<R> function) {
        this.function = function;
    }

    public R get(T key) {
        if (key == null) {
            return null;
        }
        if (values.get(key) == null) {
            R value = function.get();
            values.put(key, value);
            return value;
        } else {
            return values.get(key);
        }
    }

}
