// Copyright 2011 Google Inc. All Rights Reserved.

package org.arbeitspferde.friesian;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;

import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import javax.annotation.Nullable;

/**
 * Build an in-memory cache subject to {@link CacheBuilderSpec} definitions.
 *
 * @param <T> The type of objects that this cache can hold.
 */
public class ConfigurableMapMakerCache<T> implements Cache<T> {
  private static final Logger log =
      Logger.getLogger(ConfigurableMapMakerCache.class.getCanonicalName());
  private final ConcurrentMap<Integer, T> backingMap;

  public ConfigurableMapMakerCache(final CacheBuilderSpec specification)
      throws IllegalArgumentException {
    Preconditions.checkNotNull(specification, "specification may not be null.");
    Preconditions.checkArgument(!specification.toParsableString().isEmpty(),
        "specification may not be empty.");

    backingMap = CacheBuilder.from(specification).<Integer, T>build().asMap();

    log.info(String.format("Created map with %s specification.", specification));
  }

  @Override
  public int getCacheSize() {
    return backingMap.size();
  }

  @Override
  public void update(final int index, final T data) throws IndexOutOfBoundsException {
    Preconditions.checkArgument(index >= 0, "index must be zero or greater.");
    Preconditions.checkNotNull(data, "data may not be null.");

    backingMap.put(index, data);
  }

  @Override
  @Nullable
  public T get(final int index) {
    Preconditions.checkArgument(index >= 0, "index must be zero or greater.");

    return backingMap.get(index);
  }
}
