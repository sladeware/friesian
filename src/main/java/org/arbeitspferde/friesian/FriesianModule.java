// Copyright 2012 Google, Inc.  All rights reserved.

package org.arbeitspferde.friesian;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.arbeitspferde.friesian.utility.Settings;
import org.arbeitspferde.friesian.utility.SettingsProvider;
import org.uncommons.maths.random.MersenneTwisterRNG;

import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

/**
 * The Guice module for the Friesian workhorse.  It manages dependency injection for
 * the test application.
 */
public class FriesianModule extends AbstractModule {
  final String[] args;

  public FriesianModule(final String[] args) {
    this.args = args;
  }

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder()
    .implement(WorkEngine.class, ListWorkEngine.class)
    .build(ListWorkEngineFactory.class));

    bind(Random.class).to(MersenneTwisterRNG.class);
    bind(Stopwatch.class).annotatedWith(Names.named("serverDuration")).to(Stopwatch.class)
    .in(Singleton.class);
    bind(AtomicLong.class).annotatedWith(Names.named("jtaWorkerWorkTime")).to(AtomicLong.class)
    .in(Singleton.class);
    bind(AtomicLong.class).annotatedWith(Names.named("jtaWorkerSleepTime")).to(AtomicLong.class)
    .in(Singleton.class);
    bind(Settings.class).toProvider(SettingsProvider.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  public String[] getArguments() {
    return args;
  }
}
