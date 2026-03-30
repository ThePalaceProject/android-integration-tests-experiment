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

  /**
   * @return The list of devices used for tests
   *
   * @see "https://www.browserstack.com/list-of-browsers-and-platforms/app_automate"
   */

  public static Stream<AppiumDeviceConfiguration> deviceConfigurations()
  {
    if (inBrowserStack()) {
      return Stream.of(
        new AppiumDeviceConfiguration(
          "Samsung Galaxy S23",
          "13.0"
        ),
        new AppiumDeviceConfiguration(
          "Google Pixel 9",
          "16.0"
        ),
        new AppiumDeviceConfiguration(
          "Motorola Moto G9 Play",
          "10.0"
        ),
        new AppiumDeviceConfiguration(
          "Samsung Galaxy Tab S11",
          "16.0"
        )
      );
    } else {
      return Stream.of(
        new AppiumDeviceConfiguration(
          "ADB",
          "16.0"
        )
      );
    }
  }

  private static boolean inBrowserStack()
  {
    final var browserstackAppId =
      System.getenv("PALACE_BROWSERSTACK_APP_URL");

    return (browserstackAppId != null);
  }
}
