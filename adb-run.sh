#!/bin/sh -ex

adb kill-server
ADB_SERVER_SOCKET=tcp:5037 adb start-server
