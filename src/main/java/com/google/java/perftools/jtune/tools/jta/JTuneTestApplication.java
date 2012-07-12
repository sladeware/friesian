// Copyright 2011 Google Inc. All Rights Reserved.

package com.google.perftools.jtune.tools.jta;

import com.google.common.base.Stopwatch;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.perftools.jtune.tools.jta.open.OpenModule;
import com.google.perftools.jtune.tools.jta.utility.Metric;
import com.google.perftools.jtune.tools.jta.utility.MetricExporter;
import com.google.perftools.jtune.tools.jta.utility.Settings;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The purpose of the JTune Test Application (JTA) is to provide a simple Java application that is
 * configurable to simulate JVM memory behavior so that it can be used to verify and validate core
 * JTune features such as the Hypothesizer's generational genetic algorithm. It starts up quickly
 * and can simulate the diurnal qps load that a real user-facing Google application experiences.
 * Its multithreaded and 100% pure Java.
 *
 * Please see the design document:
 *  http://go/jtune-test-application
 */
@Singleton
public class JTuneTestApplication {
  /** Logger for this class */
  private static final Logger log = Logger.getLogger(JTuneTestApplication.class.getCanonicalName());

  private AtomicLong jtaTotalNumberOfWorkItems = new AtomicLong();
  private final AtomicLong jtaMasterSleepTime = new AtomicLong();
  private final AtomicLong jtaWorkerWorkTime = new AtomicLong();
  private final AtomicLong jtaWorkerSleepTime = new AtomicLong();

  private final Stopwatch timer;
  private final MetricExporter metricExporter;
  private final ListWorkEngineFactory listWorkEngineFactory;
  private final Settings settings;

  @Inject
  public JTuneTestApplication(Stopwatch timer, MetricExporter metricExporter,
      ListWorkEngineFactory listWorkEngineFactory, Settings settings) {
    this.timer = timer;
    this.metricExporter = metricExporter;
    this.listWorkEngineFactory = listWorkEngineFactory;
    this.settings = settings;
  }

  /** Run the JTune Test Application */
  public void run() {

    timer.start();

    final ExecutorService workService = Executors.newCachedThreadPool();

    final Cache<List<Integer>> hotCache =
        new HardReferenceInMemoryCache<List<Integer>>(settings.getHotCacheSize());
    final Cache<List<Integer>> coldCache =
        new HardReferenceInMemoryCache<List<Integer>>(settings.getColdCacheSize());

    metricExporter.init();

    metricExporter.register(
        "jta_total_number_of_work_items",
        "The total number of work items started by workers since startup",
        Metric.make(jtaTotalNumberOfWorkItems));
    metricExporter.register(
        "jta_master_sleep_time",
        "Amount of time the master slept after scheduling the current worker",
        Metric.make(jtaMasterSleepTime));
    metricExporter.register(
        "jta_worker_work_time",
        "Amount of wall clock time worker's spent in the work state since JTA startup",
        Metric.make(jtaWorkerWorkTime));
    metricExporter.register(
        "jta_worker_sleep_time",
        "The total amount of milliseconds that the workers have slept since JTA startup",
        Metric.make(jtaWorkerSleepTime));

    try {
      while (true) {
        try {
          final WorkEngine worker = listWorkEngineFactory.create(
              settings.getSleepProbability(), settings.getWorkerSleepTime(), hotCache,
              coldCache, settings.getMinListSize(), settings.getMaxListSize(),
              settings.getMinNumberOfListPartitions(), settings.getMaxNumberOfListPartitions(),
              settings.getHotProbability(), settings.getColdProbability());
          workService.execute(worker);
          jtaTotalNumberOfWorkItems.incrementAndGet();
        } catch (RejectedExecutionException e) {
          log.log(Level.WARNING, "Unable to execute thread", e);
        }
        jtaMasterSleepTime.set(howManyMillisToSleep());
        Thread.sleep(jtaMasterSleepTime.get());
      }
    } catch (InterruptedException e) {
      log.log(Level.WARNING, "Master is unable to sleep", e);
    }
  }

  /** Returns the varying number of milliseconds to sleep based on the diurnal curve function */
  private long howManyMillisToSleep() {
    return (long) (1000.0 / (settings.getRateSlopeConstant() * diurnalCurve(this.timer)
        + settings.getRateInterceptConstant()));
  }

  /**
   * Simluates a diurnal curve. Returns a number of milliseconds. d = -cos(msec_since_epoch) + 1.5
   */
  private double diurnalCurve(Stopwatch timer) {
    return -1.0 * Math.cos((timer.elapsedMillis()
        / (settings.getDiurnalPeriod() * 60.0 * 60.0 * 1000.0))
        * Math.PI * 2.0) + 1.5;
  }

  /** Program entry point */
  public static void main(String[] args) {
    try {
      final Injector injector = Guice.createInjector(new JtaModule(args), new OpenModule());

      JTuneTestApplication jtuneTestApplication = injector.getInstance(JTuneTestApplication.class);
      jtuneTestApplication.run();
    } catch (final Exception e) {
      log.log(Level.SEVERE, "Uncaught exception; aborting.", e);
      System.exit(1);
    }
  }
}
