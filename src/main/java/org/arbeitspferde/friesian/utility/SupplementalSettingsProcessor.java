// Copyright 2012 Google, Inc.  All rights reserved.

package org.arbeitspferde.friesian.utility;

/**
 * Provide a means for proprietary settings to be set based off of supplemental flag values.
 */
public interface SupplementalSettingsProcessor {
  /**
   * Process supplemental arguments unrecognized by args4j and do what is needed with them, if
   * anything.
   *
   * @throws IllegalArgumentException if an illegal argument is provided.
   */
  public void process(final String[] arguments) throws IllegalArgumentException;
}
