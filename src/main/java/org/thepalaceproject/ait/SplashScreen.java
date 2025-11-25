package org.thepalaceproject.ait;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Functions to handle the splash screen.
 */

public final class SplashScreen
{
  private static final Logger LOG =
    LoggerFactory.getLogger(SplashScreen.class);

  private SplashScreen()
  {

  }

  public static void waitForAndCompleteSplashScreen(
    final AppiumTestContext context)
  {
    waitForAndCompleteNotificationsScreen(context);
    waitForAndCompleteTutorialScreen(context);
    waitForAndCompleteLibrarySelection(context);
    waitForCatalog(context);
  }

  public static void waitForCatalog(
    final AppiumTestContext context)
  {
    LOG.debug("Waiting for the catalog screen...");

    final var driver = context.driver();

    final var wait =
      new WebDriverWait(driver, Duration.ofSeconds(30L));
    final var catalogLocator =
      By.id("catalogContentContainer");

    wait.until(ExpectedConditions.visibilityOfElementLocated(catalogLocator));
    LOG.debug("Catalog screen has arrived.");
  }

  public static void waitForAndCompleteLibrarySelection(
    final AppiumTestContext context)
  {
    LOG.debug("Waiting for the library selection screen...");

    final var driver = context.driver();

    /*
     * Wait for the list of accounts to load.
     */

    final var wait =
      new WebDriverWait(driver, Duration.ofSeconds(30L));
    final var listViewLocator =
      By.id("accountRegistryList");

    LOG.debug("Waiting for the account list to become visible...");
    final var listView =
      wait.until(ExpectedConditions.visibilityOfElementLocated(listViewLocator));
    LOG.debug("Account list has become visible.");

    LOG.debug("Waiting for the account list to become populated...");
    wait.until(new ListViewNonEmpty(listView));
    LOG.debug("Account list has become populated.");

    /*
     * Find the first element of the list and click it.
     */

    final var firstItem =
      listView.findElements(By.className("android.view.ViewGroup"))
        .get(0);

    firstItem.click();
  }

  public static void waitForAndCompleteTutorialScreen(
    final AppiumTestContext context)
  {
    LOG.debug("Waiting for the tutorial screen...");

    final var driver = context.driver();

    /*
     * Wait until the splash screen "skip" control becomes available.
     */

    final var wait =
      new WebDriverWait(driver, Duration.ofSeconds(30L));
    final var locator =
      By.id("tutorialSkipTouch");

    wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    LOG.debug("Tutorial screen has arrived.");

    /*
     * Press the "skip" control.
     */

    driver.findElement(locator).click();
    LOG.debug("Completed tutorial screen.");
  }

  public static void waitForAndCompleteNotificationsScreen(
    final AppiumTestContext context)
  {
    LOG.debug("Waiting for the notifications screen...");

    final var driver = context.driver();

    /*
     * Wait until the notifications screen "allow" control becomes available.
     */

    final var allowButtonWait =
      new WebDriverWait(driver, Duration.ofSeconds(30L));
    final var locator =
      By.id("splashNotificationsAllow");

    allowButtonWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    LOG.debug("Notifications screen has arrived.");

    /*
     * Press the "allow" control.
     */

    driver.findElement(locator).click();
    LOG.debug("Completed notifications screen.");

    /*
     * Now wait for the permission popup and accept it. We treat this as
     * something that can fail to occur, because it's entirely down to the
     * device whether this popup even appears.
     */

    try {
      final var popupWait =
        new WebDriverWait(driver, Duration.ofSeconds(5L));
      final var popupLocator =
        By.xpath("//android.widget.Button[@text='Allow']");
      final var popupCondition =
        ExpectedConditions.elementToBeClickable(popupLocator);

      popupWait.until(popupCondition).click();
    } catch (final Exception e) {
      LOG.debug("Popup wait failed: ", e);
    }
  }
}
