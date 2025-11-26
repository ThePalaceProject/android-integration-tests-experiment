package org.thepalaceproject.ait;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public final class StartupTest
{
  private AppiumTestContext context;

  @AfterEach
  public void tearDown()
    throws Exception
  {
    this.context.close();
  }

  @ParameterizedTest
  @MethodSource("org.thepalaceproject.ait.AppiumDeviceConfigurations#deviceConfigurations")
  void testHelloWorld(
    final AppiumDeviceConfiguration device,
    final TestInfo testInfo)
    throws Exception
  {
    this.context = AppiumTestContext.createForTestInfo(testInfo, device);
    SplashScreen.waitForAndCompleteSplashScreen(this.context);
  }
}
