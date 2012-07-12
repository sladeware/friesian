// Copyright 2012 Google, Inc.  All rights reserved.

package com.google.perftools.jtune.tools.jta.open;

import com.google.inject.Singleton;
import com.google.perftools.jtune.tools.jta.utility.MetricExporter;
import com.google.perftools.jtune.tools.jta.utility.MetricListener;

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
