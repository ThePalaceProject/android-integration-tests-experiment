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
public final class BookDetailsClickMoreTest
{
  private AppiumTestContext context;

  @AfterEach
  public void tearDown()
    throws Exception
  {
    this.context.close();
  }

  /**
   * Full book summary is visible after clicking More.
   */

  @ParameterizedTest
  @MethodSource("org.thepalaceproject.ait.AppiumDeviceConfigurations#deviceConfigurations")
  void testDescriptionMore(
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

    final var driver =
      this.context.driver();

    final var textElement =
      driver.findElement(By.id("bookD2Text"));
    final var moreButton =
      FindByExactText.findElementWithExactText(this.context, "More");

    final var collapsedSize =
      textElement.getSize();

    moreButton.click();

    final var expandedSize =
      textElement.getSize();

    Assertions.assertTrue(
      expandedSize.getHeight() > collapsedSize.getHeight(),
      "Clicking the More button should expand the text height."
    );
  }
}
