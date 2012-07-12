// Copyright 2012 Google, Inc.  All rights reserved.

package com.google.perftools.jtune.tools.jta.open;

import com.google.inject.Singleton;
import com.google.perftools.jtune.tools.jta.utility.SupplementalSettingsProcessor;

/**
 * Don't do anything with any supplemental flags.
 */
@Singleton
public class NullSupplementalSettingsProcessor implements SupplementalSettingsProcessor {
  @Override
  public void process(final String[] arguments) throws IllegalArgumentException {
  }
}
