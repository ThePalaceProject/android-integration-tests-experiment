android-integration-tests
===

## Usage

### APK

Place the application to be tested at `./app.apk`.

### ADB

Run `./adb-run.sh`. 
  
This ensures that `adb` is accessible over TCP port `5037`. This is necessary 
for the `appium` server to be able to talk to it. The script will run the
necessary `adb` commands and then exit.

### Appium

Run `./appium-run.sh`. 

This starts up a `podman` container running the `appium` server. The script
will continue running until it is killed.

### Build/Run Tests

Run `mvn package`.
