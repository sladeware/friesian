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
 * A read-only configuration singleton for the test application.
 */
public interface Settings {
  public Double getRateSlopeConstant();
  public Double getRateInterceptConstant();
  public Integer getMaxListSize();
  public Integer getMinListSize();
  public Integer getMaxNumberOfListPartitions();
  public Integer getMinNumberOfListPartitions();
  public Integer getHotProbability();
  public Integer getHotCacheSize();
  public Integer getColdProbability();
  public Integer getColdCacheSize();
  public Integer getWorkerSleepTime();
  public Integer getSleepProbability();
  public Double getDiurnalPeriod();
  public Integer getPort();
}
