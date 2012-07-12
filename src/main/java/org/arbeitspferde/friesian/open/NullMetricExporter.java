// Copyright 2012 Google, Inc.  All rights reserved.

package org.arbeitspferde.friesian.open;

import com.google.inject.Singleton;
import org.arbeitspferde.friesian.utility.MetricExporter;
import org.arbeitspferde.friesian.utility.MetricListener;

/**
 * A dummy implementation of {@link MetricExporter} that does nothing.
 */
@Singleton
public class NullMetricExporter implements MetricExporter {

  @Override
  public void register(final String name, final String description,
      final MetricListener<?> metric) {
  }

  @Override
  public void init() {
  }
}
