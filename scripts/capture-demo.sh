#!/usr/bin/env bash
set -euo pipefail

./gradlew --no-daemon :app:connectedDebugAndroidTest

if [[ "${DEMO_API_LEVEL}" == "36" ]]; then
  mkdir -p app/build/demo-screens
  adb pull /sdcard/Android/data/com.finalcode91.cookandpin/files/Pictures/demo/. app/build/demo-screens/
fi
