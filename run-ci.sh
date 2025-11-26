#!/bin/sh -e

fatal()
{
  echo "run-ci.sh: fatal: $1" 1>&2
  exit 1
}

info()
{
  echo "run-ci.sh: info: $1" 1>&2
}

#----------------------------------------------------------------------
# Clone the credentials to extract the browserstack configuration.
#

info "Checking for GitHub access token..."
if [ -z "${CI_GITHUB_ACCESS_TOKEN}" ]
then
  fatal "CI_GITHUB_ACCESS_TOKEN is not defined"
fi

info "Fetching BrowserStack credentials..."
git clone "https://${CI_GITHUB_ACCESS_TOKEN}@github.com/ThePalaceProject/mobile-certificates" .credentials
cp .credentials/BrowserStack/PalaceAndroidTests/browserstack.yml .
rm -rfv .credentials

#----------------------------------------------------------------------
# Ensure we have a working Android adb.
#

info "Checking Android SDK..."
export PATH="${PATH}:${ANDROID_HOME}/platform-tools"
adb --help 1>/dev/null

#----------------------------------------------------------------------
# Fetch the latest APK.
#

git clone --depth 1 https://github.com/ThePalaceProject/android-binaries
cp android-binaries/palace-debug.apk app.apk
rm -rfv android-binaries

./run.sh
