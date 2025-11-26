#!/bin/sh -ex

git clone https://github.com/ThePalaceProject/mobile-certificates .credentials
cp .credentials/BrowserStack/PalaceAndroidTests/browserstack.yml .
rm -rfv .credentials
./run.sh
