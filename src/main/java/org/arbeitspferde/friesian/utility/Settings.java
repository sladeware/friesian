// Copyright 2012 Google, Inc.  All rights reserved.

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
