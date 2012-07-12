// Copyright 2012 Google, Inc.  All rights reserved.

package org.arbeitspferde.friesian.open;

import com.google.inject.AbstractModule;
import org.arbeitspferde.friesian.utility.MetricExporter;
import org.arbeitspferde.friesian.utility.SupplementalSettingsProcessor;

/**
 * The Guice module for the open source implementation components of the Friesian workhorse.
 */
public class OpenModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(MetricExporter.class).to(NullMetricExporter.class);
    bind(SupplementalSettingsProcessor.class).to(NullSupplementalSettingsProcessor.class);
  }
}
