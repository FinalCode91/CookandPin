#!/usr/bin/env bash
set -euo pipefail

./gradlew --no-daemon :app:connectedDebugAndroidTest

if [[ "${DEMO_API_LEVEL}" == "36" ]]; then
  mkdir -p app/build/demo-screens
  adb pull /sdcard/Pictures/CookandPinDemo/. app/build/demo-screens/
fi
