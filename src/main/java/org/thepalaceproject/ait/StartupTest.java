package org.thepalaceproject.ait;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public final class StartupTest
{
  private AppiumTestContext context;

  @BeforeEach
  public void setUp()
    throws Exception
  {
    this.context = AppiumTestContext.create();
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
