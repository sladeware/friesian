// Copyright 2012 Google, Inc.  All rights reserved.

package org.arbeitspferde.friesian.open;

import com.google.inject.Singleton;
import org.arbeitspferde.friesian.utility.SupplementalSettingsProcessor;

/**
 * Don't do anything with any supplemental flags.
 */
@Singleton
public class NullSupplementalSettingsProcessor implements SupplementalSettingsProcessor {
  @Override
  public void process(final String[] arguments) throws IllegalArgumentException {
  }
}
