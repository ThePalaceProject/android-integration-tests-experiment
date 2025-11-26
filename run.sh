#!/bin/sh -e

fatal()
{
  echo "run.sh: fatal: $1" 1>&2
  exit 1
}

info()
{
  echo "run.sh: info: $1" 1>&2
}

run_cleanup()
{
  podman kill palace-ait-appium
}

info "Setting up ADB"
./adb-run.sh

info "Starting Appium"
./appium-run.sh &

trap run_cleanup EXIT HUP INT TERM

#----------------------------------------------------------------------
# Wait until the Appium container is actually up and running before
# trying to run the test suite.
#

while true;
do
  CONTAINER_STATUS=$(podman inspect --format='{{.State.Status}}' palace-ait-appium) || true
  if [ "${CONTAINER_STATUS}" = "running" ]
  then
    info "Appium appears to be running."
    sleep 5
    break
  else
    info "Appium status is currently: ${CONTAINER_STATUS}"
    sleep 1
  fi
done

info "Running test suite."
mvn clean package
