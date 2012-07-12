// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.perftools.jtune.tools.jta;

import javax.annotation.Nullable;

/**
 * Servers use a variety of in-memory cache types.  Some of these employ
 * hard references to objects, weak references that are subject to JVM memory
 * pressure, bound object count with LRU, and others that employ external cache
 * implementations such as EHCache, or even layered.  This interface allows for
 * any such special type to be tested.
 *
 * @param <T> The type of element contained within the cache.
 */
public interface Cache<T> {
  public int getCacheSize();

  /**
   * Update the cache with data
   *
   * The cache is updated at the specified index with the specified data when it is full, otherwise
   * the data is simply added to the end of the incomplete cache. This algorithm is useful in that
   * it mimics caching of data by an appliction, but is not actually intended to be used for "real"
   * data storage.
   */
  public void update(final int index, final T data) throws IndexOutOfBoundsException;

  /**
   * Retrieve the item with a given index from the cache.
   *
   * @param index The index of the requested item.
   * @return The requested item if it exists or null.
   */
  @Nullable
  public T get(final int index);
}
