// Copyright 2012 Google, Inc.  All rights reserved.

package org.arbeitspferde.friesian.utility;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Wrap a value or value provided into a {@link MetricListener} for a {@link MetricExporter}.
 */
public class Metric {
  /**
   * Produce a {@link MetricListener} for a {@link AtomicLong} that exposes whatever its
   * underlying value is to the {@link MetricExporter}.
   */
  public static MetricListener<Long> make(final AtomicLong value) {
    return new MetricListener<Long>() {
      public Long value() {
        return value.get();
      }
    };
  }
}
