#!/bin/sh -ex

run_cleanup()
{
  podman kill palace-ait-appium
}

./adb-run.sh
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
    sleep 5
    break
  else
    echo "Appium status is currently: ${CONTAINER_STATUS}"
    sleep 1
  fi
done

mvn clean package
