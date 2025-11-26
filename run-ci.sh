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
# Fetch the latest APK.
#

info "Fetching latest APK"
git clone --depth 1 https://github.com/ThePalaceProject/android-binaries
cp android-binaries/palace-debug.apk app.apk
rm -rfv android-binaries
APK_FILE=$(realpath app.apk)

#----------------------------------------------------------------------
# Upload the APK to browser stack.
#

info "Uploading APK to BrowserStack"
BROWSERSTACK_USERNAME=$(yq -r .userName < browserstack.yml)
BROWSERSTACK_ACCESS_KEY=$(yq -r .accessKey < browserstack.yml)

curl \
  -u "$BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY" \
  -o app.json \
  -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
  -F "file=@${APK_FILE}"

export PALACE_BROWSERSTACK_APP_URL=$(jq -r .app_url < app.json)

#----------------------------------------------------------------------
# Run the test suite.
#

info "Running test suite."
mvn clean package
