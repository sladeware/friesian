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


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.arbeitspferde.friesian.utility.Metric;
import org.arbeitspferde.friesian.utility.MetricExporter;
import org.arbeitspferde.friesian.utility.Settings;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * The purpose of the Friesian workhorse is to provide a simple Java application
 * that is configurable to simulate JVM memory behavior so that it can be used
 * to verify and validate core Konik features such as the Hypothesizer's
 * generational genetic algorithm. It starts up quickly and can simulate the
 * diurnal QPS load that a real user-facing application experiences.
 *
 * It's multithreaded and 100 percent pure Java with no JNI or native code.
 */
@Singleton
public class FriesianWorkhorse {
  /** Logger for this class */
  private static final Logger log = Logger.getLogger(FriesianWorkhorse.class.getCanonicalName());

  private final AtomicLong jtaTotalNumberOfWorkItems = new AtomicLong();
  private final AtomicLong jtaMasterSleepTime = new AtomicLong();
  private final AtomicLong jtaWorkerWorkTime = new AtomicLong();
  private final AtomicLong jtaWorkerSleepTime = new AtomicLong();

  private final Stopwatch timer;
  private final MetricExporter metricExporter;
  private final ListWorkEngineFactory listWorkEngineFactory;
  private final Settings settings;
  private final Build build;

  @Inject
  public FriesianWorkhorse(final Stopwatch timer, final MetricExporter metricExporter,
      final ListWorkEngineFactory listWorkEngineFactory, final Settings settings,
      final Build build) {
    this.timer = timer;
    this.metricExporter = metricExporter;
    this.listWorkEngineFactory = listWorkEngineFactory;
    this.settings = settings;
    this.build = build;
  }

  public void run() {
    log.info(build.getStamp());

    timer.start();

    final ExecutorService workService = Executors.newCachedThreadPool();

    final Cache<List<Integer>> hotCache = new HardReferenceInMemoryCache<List<Integer>>(
        settings.getHotCacheSize());
    final Cache<List<Integer>> coldCache = new HardReferenceInMemoryCache<List<Integer>>(
        settings.getColdCacheSize());

    metricExporter.init();

    metricExporter.register("jta_total_number_of_work_items",
        "The total number of work items started by workers since startup",
        Metric.make(jtaTotalNumberOfWorkItems));
    metricExporter.register("jta_master_sleep_time",
        "Amount of time the master slept after scheduling the current worker",
        Metric.make(jtaMasterSleepTime));
    metricExporter.register("jta_worker_work_time",
        "Amount of wall clock time worker's spent in the work state since JTA startup",
        Metric.make(jtaWorkerWorkTime));
    metricExporter.register("jta_worker_sleep_time",
        "The total amount of milliseconds that the workers have slept since JTA startup",
        Metric.make(jtaWorkerSleepTime));

    try {
      while (true) {
        try {
          final WorkEngine worker = listWorkEngineFactory.create(settings.getSleepProbability(),
              settings.getWorkerSleepTime(), hotCache, coldCache, settings.getMinListSize(),
              settings.getMaxListSize(), settings.getMinNumberOfListPartitions(),
              settings.getMaxNumberOfListPartitions(), settings.getHotProbability(),
              settings.getColdProbability());
          workService.execute(worker);
          jtaTotalNumberOfWorkItems.incrementAndGet();
        } catch (final RejectedExecutionException e) {
          log.log(Level.WARNING, "Unable to execute thread", e);
        }
        jtaMasterSleepTime.set(howManyMillisToSleep());
        Thread.sleep(jtaMasterSleepTime.get());
      }
    } catch (final InterruptedException e) {
      log.log(Level.WARNING, "Master is unable to sleep", e);
    }
  }

  /**
   * Returns the varying number of milliseconds to sleep based on the diurnal
   * curve function
   */
  private long howManyMillisToSleep() {
    return (long) (1000.0 / ((settings.getRateSlopeConstant() * diurnalCurve(this.timer)) + settings
        .getRateInterceptConstant()));
  }

  /**
   * Simluates a diurnal curve. Returns a number of milliseconds. d =
   * -cos(msec_since_epoch) + 1.5
   */
  private double diurnalCurve(final Stopwatch timer) {
    return (
        -1.0 * Math.cos(
            (timer.elapsed(TimeUnit.MILLISECONDS) / (settings.getDiurnalPeriod()
            * 60.0 * 60.0 * 1000.0)) * Math.PI * 2.0)) + 1.5;
  }
}
