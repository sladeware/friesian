// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.perftools.jtune.tools.jta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;

/**
 * Implements a thread-safe cache.
 *
 *  The purpose of the cache is to provide long lived objects for the JVM to manage. Random
 *
 * @param <T> the type of elements contained in the cache
 */
class HardReferenceInMemoryCache<T> implements Cache<T> {
  private static final Logger log =
      Logger.getLogger(HardReferenceInMemoryCache.class.getCanonicalName());
  private final int cacheSize;
  private final List<T> cache;

  public HardReferenceInMemoryCache(final int cacheSize) throws IllegalArgumentException {
    this.cache = Collections.synchronizedList(new ArrayList<T>());
    this.cacheSize = cacheSize;
  }

  @Override
  public int getCacheSize() {
    return cacheSize;
  }

  @Override
  public void update(final int index, final T data) throws IndexOutOfBoundsException {
    if ((index <= 0) || (index >= cacheSize)) {
      throw new IndexOutOfBoundsException();
    }
    if (cache.size() < this.cacheSize) {
      cache.add(data);
    } else {
      cache.set(index, data);
    }
  }

  @Override
  @Nullable
  public T get(final int index) {
    return cache.get(index);
  }
}
