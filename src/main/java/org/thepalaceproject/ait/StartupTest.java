package org.thepalaceproject.ait;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public final class StartupTest
{
  private AppiumTestContext context;

  @BeforeEach
  public void setUp(
    final TestInfo testInfo)
    throws Exception
  {
    this.context = AppiumTestContext.create(testInfo.getDisplayName());
  }

  @AfterEach
  public void tearDown()
    throws Exception
  {
    this.context.close();
  }

  @Test
  void testHelloWorld()
  {
    SplashScreen.waitForAndCompleteSplashScreen(this.context);
  }
}
