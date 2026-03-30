package org.thepalaceproject.ait;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public final class BookDetailsInfoTableTest
{
  private AppiumTestContext context;

  @AfterEach
  public void tearDown()
    throws Exception
  {
    this.context.close();
  }

  /**
   * Information area shows Published, Publisher, Category, Distributed by,
   * and Book format
   */

  @ParameterizedTest
  @MethodSource("org.thepalaceproject.ait.AppiumDeviceConfigurations#deviceConfigurations")
  void testInformationArea(
    final AppiumDeviceConfiguration device,
    final TestInfo testInfo)
    throws Exception
  {
    this.context = AppiumTestContext.createForTestInfo(testInfo, device);
    SplashScreen.waitForAndCompleteSplashScreen(this.context);

    final var bookRoot =
      PalaceBookshelf.openFirstBook(this.context);

    final var scrollView =
      BookDetailsViews.findBookDetailsScrollView(this.context);

    BookDetailsViews.scrollToBottom(this.context, scrollView);

    // These are the table elements that are actually guaranteed to exist
    // right now. Other values are subject to book metadata.
    FindByExactText.findElementWithExactText(this.context, "DISTRIBUTOR");
    FindByExactText.findElementWithExactText(this.context, "FORMAT");
    FindByExactText.findElementWithExactText(this.context, "LANGUAGE");
  }
}
