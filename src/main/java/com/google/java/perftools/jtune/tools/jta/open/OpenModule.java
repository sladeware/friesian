// Copyright 2012 Google, Inc.  All rights reserved.

package com.google.perftools.jtune.tools.jta.open;

import com.google.inject.AbstractModule;
import com.google.perftools.jtune.tools.jta.utility.MetricExporter;
import com.google.perftools.jtune.tools.jta.utility.SupplementalSettingsProcessor;

/**
 * The Guice module for the open source implementation components of the JTune Test Application.
 */
public class OpenModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(MetricExporter.class).to(NullMetricExporter.class);
    bind(SupplementalSettingsProcessor.class).to(NullSupplementalSettingsProcessor.class);
  }
}
