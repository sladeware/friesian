// Copyright 2011 Google Inc. All Rights Reserved.

package org.arbeitspferde.friesian;

import com.google.common.base.Preconditions;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates random numbers
 */
class RandomNumber {
  private static final Logger log = Logger.getLogger(RandomNumber.class.getCanonicalName());

  /** Generates a random percentage value, which is an integer from 1 to 100 inclusive */
  public static int generatePercentage(Random rng) {
    return generate(1, 100, rng);
  }

  /** Generates a random number based on the input max and min parameters */
  public static int generate(int min, int max, Random rng) {
    Preconditions.checkArgument(max > 0, "Max must be positive");
    Preconditions.checkArgument(min <= max, "Min must be less than or equal to max");

    int range = max - min;
    int result = min;

    if (range == 0) {
      return min;
    }

    try {
      result = min + rng.nextInt(range + 1);
    } catch (Exception e) {
      log.log(Level.WARNING, String.format("min: %d  min: %d", min, max), e);
    }

    return result;
  }
}
