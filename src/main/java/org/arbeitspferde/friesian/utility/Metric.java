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
