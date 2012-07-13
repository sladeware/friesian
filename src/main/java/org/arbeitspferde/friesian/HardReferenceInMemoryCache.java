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
