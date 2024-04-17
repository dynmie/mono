package me.dynmie.mono.shared.store;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author dynmie
 */
public abstract class Store<K, V> {

    public Store() {
        configure();
    }

    private final BiMap<K, V> store = HashBiMap.create();

    public V get(K key) {
        return store.get(key);
    }

    public K inverseGet(V value) {
        return store.inverse().get(value);
    }

    protected void put(K key, V value) {
        store.put(key, value);
    }

    public abstract void configure();
}
