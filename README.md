# Cook & Pin

An offline Android cookbook with nine built-in recipes. Discover recipes by name or ingredient, pin favorites, and check off ingredients in a shopping list grouped by recipe. Pins and checkmarks are stored on the device.

## Open and build

Open this folder in Android Studio and install Android SDK 36 and JDK 17 if prompted. The project uses Android Gradle Plugin 8.13.2 and the included Gradle 8.13 wrapper. Run the `app` configuration on an Android emulator or device (Android 6.0 or newer), or run `./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug` from a terminal after configuring the Android SDK.

The proposed Play package name is `com.finalcode91.cookandpin`. Confirm the name before the first Play Console upload because it identifies the app and cannot be renamed in that listing later.

For release and tester tasks, see [launch checks](docs/launch-checklist.md).

## How it works

- **Discover:** scroll or search recipes by name and ingredient; tap a card to see details or tap Pin. On a detail page, use + or − to choose half, original, one-and-a-half, or double batch sizes. The estimated portions and ingredient amounts update and are remembered for each recipe.
- **My Pins:** see saved recipes and remove them with Unpin.
- **Shopping List:** view each pinned recipe's adjusted ingredients and check off items. Changing its batch size or removing its pin clears that recipe's checkmarks.

Recipes are included in the app; there is no account, sync, recipe editor, or online feed. Times and portions are estimates; changing batch size adjusts ingredient amounts but not cooking steps, pan sizes, or cooking times. The shopping list groups items by recipe rather than adding quantities across recipes. The original recipe collection was expanded with quantities and complete directions; the pumpkin pie baking sequence follows [LIBBY'S pie guidance](https://www.verybestbaking.com/libbys/recipes/libby-s-famous-pumpkin-pie/) and meat temperature checks follow [USDA guidance](https://www.fsis.usda.gov/food-safety/safe-food-handling-and-preparation/food-safety-basics/safe-temperature-chart).
