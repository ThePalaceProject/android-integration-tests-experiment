#!/bin/sh -e

adb kill-server
ADB_SERVER_SOCKET=tcp:5037 adb start-server
