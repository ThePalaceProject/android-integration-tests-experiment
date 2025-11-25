package org.thepalaceproject.ait;

import com.io7m.jmulticlose.core.CloseableCollection;
import com.io7m.jmulticlose.core.CloseableCollectionType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public final class AppiumTestContext implements AutoCloseable
{
  private static final Logger LOG =
    LoggerFactory.getLogger(AppiumTestContext.class);

  private final CloseableCollectionType<?> resources;
  private final AndroidDriver driver;

  private AppiumTestContext(
    final CloseableCollectionType<?> resources,
    final AndroidDriver driver)
  {
    this.resources = resources;
    this.driver = driver;
  }

  public static AppiumTestContext create()
    throws Exception
  {
    LOG.debug("Setting up Appium context...");

    final var resources = CloseableCollection.create();
    final AndroidDriver driver;

    try {
      LOG.debug("Opening Android driver...");

      final var opts =
        new UiAutomator2Options()
          .setPlatformName("Android")
          .setApp("/app.apk");

      driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), opts);
      LOG.debug("Opened Android driver.");
      resources.add(driver::quit);
    } catch (final Throwable e) {
      resources.close();
      throw e;
    }

    LOG.debug("Created Appium context.");
    return new AppiumTestContext(resources, driver);
  }

  public AndroidDriver driver()
  {
    return this.driver;
  }

  @Override
  public void close()
    throws Exception
  {
    this.resources.close();
  }
}
