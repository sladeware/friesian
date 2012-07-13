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

package org.arbeitspferde.friesian;

import com.google.inject.assistedinject.Assisted;

import java.util.List;

/**
 * A means of provisioning objects via Guice that have dependencies that Guice does not fulfill
 * itself.
 */
interface ListWorkEngineFactory {
  ListWorkEngine create(@Assisted("sleepProbability") int sleepProbability,
      @Assisted("workerSleepTime") int workerSleepTime,
      @Assisted("hotCache") Cache<List<Integer>> hotCache,
      @Assisted("coldCache") Cache<List<Integer>> coldCache,
      @Assisted("minListSize") int minListSize,
      @Assisted("maxListSize") int maxListSize,
      @Assisted("minNumberOfListPartitions") int minNumberOfListPartitions,
      @Assisted("maxNumberOfListPartitions") int maxNumberOfListPartitions,
      @Assisted("hotProbability") int hotProbability,
      @Assisted("coldProbability") int coldProbability);
}
