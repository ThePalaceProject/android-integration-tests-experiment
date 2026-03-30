/*
 * Copyright © 2025 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */


package org.thepalaceproject.ait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Palace Bookshelf functions.
 */

public final class PalaceBookshelf
{
  private static final Logger LOG =
    LoggerFactory.getLogger(PalaceBookshelf.class);

  private static final Set<String> DPLA_BOOK_TITLES =
    Set.of(
      "Dobbs v. Jackson Women's Health Organization",
      "End of Affirmative Action: Students for Fair Admissions",
      "January 6th on the Record",
      "Report On The Investigation Into Russian Interference In The 2016 Presidential Election",
      "Report of Investigation Into Allegations of Sexual Harassment by Governer Andrew M. Cuomo",
      "Report of Russian Active Measures Campaigns and Interference in the 2016 U.S. Election",
      "The Covid Archive",
      "The Impeachment Papers II",
      "The Impeachment Papers",
      "U.S. Leadership in Post-War Europe"
    );

  private PalaceBookshelf()
  {

  }

  private static List<WebElement> findElementsWithContentDescriptionContaining(
    final AndroidDriver driver,
    final String text)
  {
    // XXX: Needs string escaping.
    return driver.findElements(AppiumBy.androidUIAutomator(
      "new UiSelector().descriptionContains(\"%s\")"
        .formatted(text)
    ));
  }

  private static WebElement findFirstUsableBookCover(
    final AndroidDriver driver)
  {
    for (final var bookTitle : DPLA_BOOK_TITLES) {
      final var elements =
        findElementsWithContentDescriptionContaining(
          driver,
          bookTitle
        );
      if (!elements.isEmpty()) {
        LOG.info("Opening book cover: {}", bookTitle);
        return elements.get(0);
      }
    }
    throw new IllegalStateException(
      "Failed to find a usable book cover for any title."
    );
  }

  /**
   * Try to open the first available book on the Palace Bookshelf.
   *
   * @param context The context
   *
   * @return The element
   *
   * @throws InterruptedException On interruption
   */

  public static WebElement openFirstBook(
    final AppiumTestContext context)
    throws InterruptedException
  {
    final var driver = context.driver();

    LOG.debug("Waiting for catalogGroupsEntries to appear...");
    final var catalogGroupsLocator =
      By.id("catalogGroupsEntries");
    final var catalogGroupsWait =
      new WebDriverWait(driver, Duration.ofSeconds(30L));
    catalogGroupsWait.until(
      ExpectedConditions.visibilityOfElementLocated(catalogGroupsLocator)
    );

    LOG.debug("Locating a usable book cover.");
    final var coverElement = findFirstUsableBookCover(driver);
    coverElement.click();

    LOG.debug("Waiting for bookD2Root to appear...");
    final var bookD2RootLocator =
      By.id("bookD2Root");
    final var bookD2Wait =
      new WebDriverWait(driver, Duration.ofSeconds(30L));
    bookD2Wait.until(
      ExpectedConditions.visibilityOfElementLocated(bookD2RootLocator)
    );

    final var bookRoot = driver.findElement(bookD2RootLocator);
    Objects.requireNonNull(bookRoot, "BookRoot");
    return bookRoot;
  }
}
