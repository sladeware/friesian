// Copyright 2012 Google, Inc.  All rights reserved.

package org.arbeitspferde.friesian;

import com.google.inject.assistedinject.Assisted;

import java.util.List;

/**
 * A means of provisioning objects via Guice that have dependencies that Guice does not fulfill
 * itself.
 */
interface ListWorkEngineFactory {
  ListWorkEngine create(@Assisted("sleepProbability") int sleepProbability,
      @Assisted("workerSleepTime") int workerSleepTime,
      @Assisted("hotCache") Cache<List<Integer>> hotCache,
      @Assisted("coldCache") Cache<List<Integer>> coldCache,
      @Assisted("minListSize") int minListSize,
      @Assisted("maxListSize") int maxListSize,
      @Assisted("minNumberOfListPartitions") int minNumberOfListPartitions,
      @Assisted("maxNumberOfListPartitions") int maxNumberOfListPartitions,
      @Assisted("hotProbability") int hotProbability,
      @Assisted("coldProbability") int coldProbability);
}
