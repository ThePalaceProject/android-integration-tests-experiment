#!/bin/sh -ex

IMAGE="docker.io/appium/appium:v3.1.1-p0"
APPLICATION_APK="$(realpath app.apk)"

podman run \
-i \
-t \
-e ADB_SERVER_SOCKET=tcp:127.0.0.1:5037 \
--rm \
--volume "${APPLICATION_APK}:/app.apk:ro" \
--network host \
--name palace-ait-appium \
"${IMAGE}"
