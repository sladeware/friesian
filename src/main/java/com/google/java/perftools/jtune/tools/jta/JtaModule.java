// Copyright 2012 Google, Inc.  All rights reserved.

package com.google.perftools.jtune.tools.jta;

import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.google.perftools.jtune.tools.jta.utility.Settings;
import com.google.perftools.jtune.tools.jta.utility.SettingsProvider;

import org.uncommons.maths.random.MersenneTwisterRNG;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The Guice module for the JTune Test Application.  It manages dependency injection for
 * the test application.
 */
public class JtaModule extends AbstractModule {
  final String[] args;

  public JtaModule(final String[] args) {
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
