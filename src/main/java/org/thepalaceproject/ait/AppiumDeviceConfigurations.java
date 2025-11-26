package org.thepalaceproject.ait;

import java.util.stream.Stream;

/**
 * The set of device configurations we use to run each test.
 */

public final class AppiumDeviceConfigurations
{
  private AppiumDeviceConfigurations()
  {

  }

  public static Stream<AppiumDeviceConfiguration> deviceConfigurations()
  {
    return Stream.of(
      new AppiumDeviceConfiguration(
        "Samsung Galaxy S22",
        "12.0"
      )
    );
  }
}
