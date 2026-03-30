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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FindByExactText
{
  private static final Logger LOG =
    LoggerFactory.getLogger(FindByExactText.class);

  private FindByExactText()
  {

  }

  /**
   * Find an element containing exactly the given text.
   *
   * @param context The context
   * @param text    The text
   *
   * @return The element
   *
   * @throws NoSuchElementException If an element cannot be found
   */

  public static WebElement findElementWithExactText(
    final AppiumTestContext context,
    final String text)
    throws NoSuchElementException
  {
    final var driver = context.driver();
    // XXX: Needs string escaping.

    try {
      return driver.findElement(
        AppiumBy.androidUIAutomator(
          "new UiSelector().text(\"%s\")" .formatted(text)
        )
      );
    } catch (final NoSuchElementException e) {
      LOG.error("Could not find element with text: {}", text);
      throw e;
    }
  }
}
