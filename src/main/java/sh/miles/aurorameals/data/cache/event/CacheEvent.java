package sh.miles.aurorameals.data.cache.event;

import org.jetbrains.annotations.NotNull;

public interface CacheEvent<T> {

    void onCacheAction(@NotNull final T changedElement, @NotNull final CacheOperation operation);

}
