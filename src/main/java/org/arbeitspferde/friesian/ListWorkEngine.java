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

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A work engine that works on a list of integers, by partitioning and sorting each partition
 */
class ListWorkEngine extends WorkEngine {
  private final Random rng;
  private final Cache<List<Integer>> hotCache;
  private final Cache<List<Integer>> coldCache;
  private final int listSize;
  private final int numberOfPartitions;
  private final int maxNumberOfListPartitions;
  private final int hotProbability;
  private final int coldProbability;
  private final List<Integer> workList;

  private static final Logger log = Logger.getLogger(ListWorkEngine.class.getCanonicalName());

  private boolean notFinished = true;
  private int partitionMax = 0;
  private int partitionMin = 0;
  private int partitionCount = 0;

  @Inject
  ListWorkEngine(Random rng, @Assisted("sleepProbability") int sleepProbability,
      @Assisted("workerSleepTime") int workerSleepTime,
      @Assisted("hotCache") Cache<List<Integer>> hotCache,
      @Assisted("coldCache") Cache<List<Integer>> coldCache,
      @Assisted("minListSize") int minListSize,
      @Assisted("maxListSize") int maxListSize,
      @Assisted("minNumberOfListPartitions") int minNumberOfListPartitions,
      @Assisted("maxNumberOfListPartitions") int maxNumberOfListPartitions,
      @Assisted("hotProbability") int hotProbability,
      @Assisted("coldProbability") int coldProbability,
      @Named("jtaWorkerWorkTime") AtomicLong jtaWorkerWorkTime,
      @Named("jtaWorkerSleepTime") AtomicLong jtaWorkerSleepTime) {

    super(rng, sleepProbability, workerSleepTime, jtaWorkerWorkTime, jtaWorkerSleepTime);

    this.rng = rng;
    this.hotCache = hotCache;
    this.coldCache = coldCache;
    this.listSize = RandomNumber.generate(minListSize, maxListSize, this.rng);
    this.numberOfPartitions = RandomNumber.generate(minNumberOfListPartitions,
        maxNumberOfListPartitions, this.rng);
    this.hotProbability = hotProbability;
    this.coldProbability = coldProbability;
    this.maxNumberOfListPartitions = maxNumberOfListPartitions;
    this.workList = Lists.newArrayList();
  }

  @Override
  /** Initialize the work performed by the work engine */
  public void init() {
    for (int i = 0; i < this.listSize; i++) {
      this.workList.add(RandomNumber.generate(0, Integer.MAX_VALUE - 1, this.rng));
    }
  }

  @Override
  /** Returns true when work is not finished */
  public boolean workNotFinished() {
    return notFinished;
  }

  @Override
  /** Sort a partition of the work list */
  public void doWork() {
    if (createPartition()) {
      sortPartition();
    }
  }

  @Override
  /** Possibly cache the work list into the hot or cold caches */
  public void cache() {
    Cache<List<Integer>> cache = null;
    int randomNumber = RandomNumber.generatePercentage(this.rng);

    if (randomNumber < this.hotProbability) {
      cache = this.hotCache;
    } else if (randomNumber < this.coldProbability + this.hotProbability) {
      cache = this.coldCache;
    }
    if (cache != null) {
      List<Integer> retrieved;
      try {
        final int index = RandomNumber.generate(0, cache.getCacheSize() - 1, this.rng);
        retrieved = cache.get(index);
        cache.update(index, workList);
      } catch (IllegalArgumentException e) {
        log.log(Level.WARNING, "Problems updating the cache", e);
      } finally {
        retrieved = null;
      }
    }
  }

  /** Sort the partition of the work list into ascending order */
  private void sortPartition() {
    List<Integer> list = Lists.newArrayList();

    for (int i = this.partitionMin; i <= this.partitionMax; i++) {
      list.add(this.workList.get(i));
    }

    Collections.sort(list);

    for (int i = this.partitionMin; i <= this.partitionMax; i++) {
      try {
        this.workList.set(i, list.get(i - this.partitionMin));
      } catch (Exception e) {
        log.log(Level.WARNING, "Problems sorting the partition", e);
      }
    }
  }

  /** Create the next partition of the work list and return false if not created, otherwise true */
  private boolean createPartition() {
    this.partitionMin = this.partitionMax + 1;
    if (this.partitionMin >= this.listSize) {
      this.workFinished();
      return false;
    }

    this.partitionMax += this.listSize / this.numberOfPartitions;
    this.partitionCount += 1;

    if ((this.partitionMax >= this.listSize) ||
        (this.partitionCount > this.maxNumberOfListPartitions)) {
      this.partitionMax = this.listSize - 1;
    }

    if (this.partitionMin >= this.partitionMax) {
      this.workFinished();
      return false;
    }

    return true;
  }

  /** Signify that work is complete */
  private void workFinished() {
    notFinished = false;
  }
}
