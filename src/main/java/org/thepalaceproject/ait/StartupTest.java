package org.thepalaceproject.ait;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public final class StartupTest
{
  private static AppiumTestContext context;

  @BeforeAll
  static void setUp()
    throws Exception
  {
    context = AppiumTestContext.create();
  }

  @Test
  void testHelloWorld()
  {

  }

  @AfterAll
  static void tearDown()
    throws Exception
  {
    context.close();
  }
}
