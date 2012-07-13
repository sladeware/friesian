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
public class BaseModule extends AbstractModule {
  final String[] args;

  public BaseModule(final String[] args) {
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
