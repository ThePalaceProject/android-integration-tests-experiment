package org.thepalaceproject.ait;

/**
 * A single device configuration.
 *
 * @param deviceName The device name. For example, "Samsung Galaxy S22".
 * @param osVersion  The OS version. For example, "12.0".
 */

public record AppiumDeviceConfiguration(
  String deviceName,
  String osVersion)
{

}
