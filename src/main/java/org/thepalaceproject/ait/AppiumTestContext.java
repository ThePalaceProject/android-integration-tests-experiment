package org.thepalaceproject.ait;

import com.io7m.jmulticlose.core.CloseableCollection;
import com.io7m.jmulticlose.core.CloseableCollectionType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

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

  public static AppiumTestContext createForTestInfo(
    final TestInfo testInfo,
    final AppiumDeviceConfiguration deviceConfiguration)
    throws Exception
  {
    return create(
      AppiumTestContext.createTestName(testInfo),
      deviceConfiguration
    );
  }

  public static AppiumTestContext create(
    final String testName,
    final AppiumDeviceConfiguration deviceConfiguration)
    throws Exception
  {
    LOG.debug("Setting up Appium context...");

    final var browserstackAppId =
      System.getenv("PALACE_BROWSERSTACK_APP_URL");
    if (browserstackAppId != null) {
      return createForBrowserstack(
        testName,
        browserstackAppId,
        deviceConfiguration
      );
    }
    return createForLocal(testName);
  }

  private static AppiumTestContext createForBrowserstack(
    final String testName,
    final String appId,
    final AppiumDeviceConfiguration deviceConfiguration)
    throws Exception
  {
    final var resources =
      CloseableCollection.create();

    final AndroidDriver driver;

    try {
      LOG.debug("Opening Android driver...");

      final var username =
        System.getenv("PALACE_BROWSERSTACK_USERNAME");
      final var accessKey =
        System.getenv("PALACE_BROWSERSTACK_ACCESS_KEY");
      final var gitCommit =
        System.getenv("PALACE_GIT_COMMIT");

      Objects.requireNonNull(username, "PALACE_BROWSERSTACK_USERNAME");
      Objects.requireNonNull(accessKey, "PALACE_BROWSERSTACK_ACCESS_KEY");
      Objects.requireNonNull(gitCommit, "PALACE_GIT_COMMIT");

      final var browserstackOptions = new HashMap<String, Object>();
      browserstackOptions.put("userName", username);
      browserstackOptions.put("accessKey", accessKey);
      browserstackOptions.put("buildName", gitCommit);
      browserstackOptions.put("sessionName", testName);

      final var caps = new DesiredCapabilities();
      caps.setCapability("appium:deviceName", deviceConfiguration.deviceName());
      caps.setCapability("appium:os_version", deviceConfiguration.osVersion());
      caps.setCapability("appium:app", appId);
      caps.setCapability("platformName", "Android");
      caps.setCapability("bstack:options", browserstackOptions);

      driver = new AndroidDriver(
        new URL("https://hub.browserstack.com/wd/hub"),
        caps);
      LOG.debug("Opened Android driver.");
      resources.add(driver::quit);
    } catch (final Throwable e) {
      resources.close();
      throw e;
    }

    LOG.debug("Created Appium context.");
    return new AppiumTestContext(resources, driver);
  }

  private static AppiumTestContext createForLocal(
    final String testName)
    throws Exception
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

  private static String createTestName(
    final TestInfo testInfo)
  {
    final var text = new StringBuilder();
    final var testClass = testInfo.getTestClass();
    if (testClass.isPresent()) {
      text.append(testClass.get().getSimpleName());
      text.append('.');
    }

    text.append(testInfo.getDisplayName());
    return text.toString();
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
