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
