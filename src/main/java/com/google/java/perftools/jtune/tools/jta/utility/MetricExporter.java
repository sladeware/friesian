// Copyright 2012 Google, Inc.  All rights reserved.

package com.google.perftools.jtune.tools.jta.utility;

/**
 * {@link MetricExporter} is a means of exposing metrics of a given name to a time series processor
 * or experimental test harness.  The default implementation {@link NullMetricExporter} simply does
 * nothing.
 */
public interface MetricExporter {
  /**
   * Register a metric with the exporter.  The exporter is expected to maintain a reference to
   * the listener.
   */
  public void register(final String name, final String description, final MetricListener<?> metric);

  /**
   * Ready the exporter for use.
   */
  public void init();
}
