package org.thepalaceproject.ait;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * A condition that can be used to wait until a recycler view is non-empty.
 */

public final class ListViewNonEmpty
  implements ExpectedCondition<WebElement>
{
  private static final By CLASS_NAME_LOCATOR =
    By.className("android.view.ViewGroup");

  private final WebElement listView;

  public ListViewNonEmpty(
    WebElement listView)
  {
    this.listView = listView;
  }

  @Override
  public WebElement apply(
    final WebDriver input)
  {
    if (!this.listView.findElements(CLASS_NAME_LOCATOR).isEmpty()) {
      return this.listView.findElements(CLASS_NAME_LOCATOR).get(0);
    }
    return null;
  }

  @Override
  public boolean equals(
    final @Nullable Object obj)
  {
    return false;
  }
}
