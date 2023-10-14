package sh.miles.aurorameals.data.cache;

import org.jetbrains.annotations.NotNull;
import sh.miles.aurorameals.data.cache.event.CacheEvent;
import sh.miles.aurorameals.data.cache.event.CacheOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Simple Cache that can have events
 */
public abstract class SimpleCache<K, V> {

    protected final Map<K, V> cache;
    private final Set<CacheEvent<V>> events;

    protected SimpleCache() {
        this.cache = new HashMap<>();
        this.events = new HashSet<>();
    }

    public void add(K key, V value) {
        cache.put(key, value);
        watch(value, CacheOperation.ADD);
    }

    public void remove(K key) {
        final V value = cache.remove(key);
        if (value == null) {
            return;
        }
        watch(value, CacheOperation.REMOVE);
    }

    public void operate(K key, Consumer<V> consumer) {
        final V value = cache.get(key);
        if (value == null) {
            return;
        }
        consumer.accept(value);
        watch(value, CacheOperation.UPDATE);
    }

    @NotNull
    public List<V> toList() {
        final List<V> list = new ArrayList<>();
        cache.forEach((k, v) -> list.add(v));
        return list;
    }

    @NotNull
    public List<V> toList(Predicate<V> filter) {
        final List<V> list = new ArrayList<>();
        cache.forEach((k, v) -> {
            if (filter.test(v)) {
                list.add(v);
            }
        });
        return list;
    }

    public void subscribe(final CacheEvent<V> listener) {
        events.add(listener);
    }

    private void watch(final V changed, CacheOperation operation) {
        for (CacheEvent<V> event : events) {
            event.onCacheAction(changed, operation);
        }
    }
}
