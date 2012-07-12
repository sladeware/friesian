// Copyright 2012 Google, Inc.  All rights reserved.

package com.google.perftools.jtune.tools.jta.utility;

/**
 * Provide a means of exposing some telemetry source's values to a {@link MetricExporter} for
 * consumption.
 */
public interface MetricListener<T> {
  /**
   * Emit the instantaneous telemetry source's value.
   */
  public T value();
}
