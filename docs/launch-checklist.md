# Cook & Pin launch checks

Target: Google Play testing, then production. Treat the first Play upload package name `com.finalcode91.cookandpin` as a proposal until the owner confirms it. Once used for a Play listing, that package name cannot simply be swapped for a later update.

## Week 1 — first installable test

- [ ] Confirm the Play package name, app ownership and usage rights for the nine recipe photos and launcher art.
- [ ] Wait for the Android checks workflow on the pull request to pass: unit tests, lint, debug APK, and release bundle.
- [ ] Build and run on a real phone with Android 16/API 36 and one older Android phone (Android 6+/API 23 if available). Check light and dark themes, large font, and screen rotation.
- [ ] In Android Studio, use **Build > Generate Signed Bundle/APK > Android App Bundle**. Create an upload key if needed; keep the key and password privately backed up. Do not commit either to GitHub. Use Play App Signing for distribution.
- [ ] Complete the Play Console app setup and listing, including content rating, target audience, Data safety answers matching the actual app, screenshots, and contact information. Check the Play pre-launch report after uploading.
- [ ] Start internal testing, then closed testing as soon as the release is stable enough for testers.

## Weeks 2–3 — tester run

If the Play Console uses a personal developer account created after November 13, 2023, production access requires **12 testers opted in continuously for at least 14 days** in the *closed* test. Internal testers alone do not satisfy this. Recruit extra people in case someone opts out. Record the start date and keep the closed test running while fixing issues.

Give each tester these tasks without explaining the screens first:

1. Find a recipe by name, then by an ingredient. Open it and follow the instructions.
2. Pin two recipes, close and reopen the app, and verify that My Pins still shows them.
3. Change one recipe to half and another to double. Compare the amounts in the detail page and Shopping List. Try pumpkin pie and chocolate cake only after reviewing pan and bake guidance.
4. Check two shopping ingredients, close and reopen the app, and verify the checkmarks. Change the batch size and note that the recipe's checkmarks reset.
5. Unpin one recipe; verify that it disappears from My Pins and its ingredients disappear from Shopping List.
6. Use a small phone and the largest font size you normally use. Try both light and dark mode.

Ask each tester: device model, Android version, app version, exact steps, expected result, actual result, screenshot or screen recording if possible, and whether they would use the app again. Have them report confusing behavior even if nothing crashes. Review Play Console's pre-launch report and testing feedback daily.

## Week 4 — release gate

- [ ] All automated checks pass on the exact commit being released.
- [ ] At least two real-device smoke tests pass, including a clean install and an upgrade that keeps pins and shopping progress.
- [ ] No unresolved crash, broken navigation, missing recipe, incorrect ingredient quantity, or inaccessible primary action.
- [ ] Testers understand that pinning a recipe adds its ingredients to Shopping List. Revisit the behavior if it caused repeated confusion.
- [ ] Verify recipe instructions against cooked results, especially scaled baked recipes and food temperatures.
- [ ] If the closed-test rule applies, apply for production access after the continuous 14-day requirement. Allow time for review; approval is not automatic.
- [ ] Generate a signed release bundle, increment `versionCode` for every later Play upload, inspect the final listing, and submit only after the checks above.

Sources: [Google Play target API requirements](https://support.google.com/googleplay/android-developer/answer/11926878), [closed testing requirements](https://support.google.com/googleplay/android-developer/answer/14151465), [Android app signing](https://developer.android.com/studio/publish/app-signing).
