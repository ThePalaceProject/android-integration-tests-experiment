#!/bin/sh -ex

fatal()
{
  echo "run-ci.sh: fatal: $1" 1>&2
  exit 1
}

if [ -z "${CI_GITHUB_ACCESS_TOKEN}" ]
then
  fatal "CI_GITHUB_ACCESS_TOKEN is not defined"
fi

git clone "https://${CI_GITHUB_ACCESS_TOKEN}:github.com/ThePalaceProject/mobile-certificates" .credentials
cp .credentials/BrowserStack/PalaceAndroidTests/browserstack.yml .
rm -rfv .credentials

./run.sh
