package org.thepalaceproject.ait;

import com.io7m.jmulticlose.core.CloseableCollection;
import com.io7m.jmulticlose.core.CloseableCollectionType;
import com.io7m.jmulticlose.core.ClosingResourceFailedException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    final var browserstackAppId =
      System.getenv("PALACE_BROWSERSTACK_APP_URL");
    if (browserstackAppId != null) {
      return createForBrowserstack(browserstackAppId);
    }

    return createForLocal();
  }

  private static AppiumTestContext createForBrowserstack(
    final String appId)
    throws Exception
  {
    final var resources =
      CloseableCollection.create();

    final AndroidDriver driver;

    try {
      LOG.debug("Opening Android driver...");

      final var caps = new DesiredCapabilities();
      caps.setCapability("platformName", "Android");
      caps.setCapability("app", appId);
      caps.setCapability("automationName", "UIAutomator2");

      final var bstackOpts = new HashMap<>();
      bstackOpts.put("deviceName", "Google Pixel 7");
      bstackOpts.put("osVersion", "13.0");
      bstackOpts.put("projectName", "PalaceIntegrationTests");
      bstackOpts.put("buildName", "Build XYZ");
      bstackOpts.put("sessionName", "My test run");
      bstackOpts.put("appiumVersion", "2.19.0");
      bstackOpts.put("debug", true);
      bstackOpts.put("video", true);

      caps.setCapability("bstack:options", bstackOpts);
      driver = new AndroidDriver(new URL("https://hub.browserstack.com/wd/hub"), caps);
      LOG.debug("Opened Android driver.");
      resources.add(driver::quit);
    } catch (final Throwable e) {
      resources.close();
      throw e;
    }

    LOG.debug("Created Appium context.");
    return new AppiumTestContext(resources, driver);
  }

  private static AppiumTestContext createForLocal()
    throws ClosingResourceFailedException, MalformedURLException
  {
    final var resources =
      CloseableCollection.create();

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
