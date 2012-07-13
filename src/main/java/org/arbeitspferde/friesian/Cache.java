/* Copyright 2012 Google, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package org.arbeitspferde.friesian;

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
