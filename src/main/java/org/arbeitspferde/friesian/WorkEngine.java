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

import com.google.common.base.Stopwatch;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This abstract class represents units of work performed by worker threads
 *
 * Each worker partitions a list and sorts each partition sequentially. Partitions are equally size,
 * except possibly the last one. The number of partitions is chosen randomly. A worker may randomly
 * sleep between partition sorts (mimicking blocking on I/O). One of two caches is potentially
 * updated when the workers are done sorting partitions of the list.
 */
abstract class WorkEngine implements Runnable {
  private static final Logger log = Logger.getLogger(WorkEngine.class.getCanonicalName());

  private final Random rng;
  private final int sleepProbability;
  private final int workerSleepTime;
  private final AtomicLong jtaWorkerWorkTime;
  private final AtomicLong jtaWorkerSleepTime;

  WorkEngine(Random rng, int sleepProbability, int workerSleepTime,
      AtomicLong jtaWorkerWorkTime, AtomicLong jtaWorkerSleepTime) {
    this.rng = rng;
    this.sleepProbability = sleepProbability;
    this.workerSleepTime = workerSleepTime;
    this.jtaWorkerWorkTime = jtaWorkerWorkTime;
    this.jtaWorkerSleepTime = jtaWorkerSleepTime;
  }

  public void run() {
    long startMillis;
    init();
    while (workNotFinished()) {
      // TODO(mtp): This introduces non-determinism for testing; fix.
      final Stopwatch timer = new Stopwatch().start();
      doWork();
      timer.stop();
      jtaWorkerWorkTime.addAndGet(timer.elapsed(TimeUnit.MILLISECONDS));
      if (sleepProbability >= RandomNumber.generatePercentage(rng)) {
        try {
          Thread.sleep(workerSleepTime);
          jtaWorkerSleepTime.addAndGet(workerSleepTime);
        } catch (InterruptedException e) {
          log.log(Level.WARNING, "Worker is unable to sleep", e);
        }
      }
    }
    cache();
  }

  /** Initialize the work engine */
  abstract void init();

  /** Returns true when all work is done, otherwise returns false. */
  abstract boolean workNotFinished();

  /** Performs a unit of work */
  abstract void doWork();

  /** Possibly caches the work performed in the hot or cold cache */
  abstract void cache();
}
