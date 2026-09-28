# cookandpin launch checks

Target: Google Play testing, then production. Use `cookandpin` as the visible app name and `com.finalcode91.cookandpin` as its required dotted Android application ID. Once used for a Play listing, that ID cannot simply be swapped for a later update.

## Week 1 — first installable test

- [ ] Check that the Play listing uses `cookandpin` and `com.finalcode91.cookandpin`; confirm app ownership and usage rights for the nine existing recipe photos and launcher art (the 20 new recipes use neutral artwork).
- [ ] Sign in to [Play Console](https://play.google.com/console/) to check whether a developer account already exists. If it does not, register this week: Google lists a US$25 one-time fee and developer identity verification; new personal accounts also require Android device verification. Leave time to finish these steps before the closed test.
- [ ] Wait for the Android checks workflow on the pull request to pass: unit tests, lint, debug APK, and release bundle.
- [ ] Check that the API 23 and API 36 emulator smoke jobs pass. They repeatedly search, navigate between tabs, and recreate the app activity. These checks run for each pull request but cannot replace physical phone testing.
- [ ] Build and run on a real phone with Android 16/API 36 and one older Android phone (Android 6+/API 23 if available). Check light and dark themes, large font, and screen rotation.
- [ ] In Android Studio, use **Build > Generate Signed Bundle/APK > Android App Bundle**. Create an upload key if needed; keep the key and password privately backed up. Do not commit either to GitHub. Use Play App Signing for distribution.
- [ ] Complete the Play Console app setup and listing, including content rating, target audience, Data safety answers matching the actual app, screenshots, and contact information. Check the Play pre-launch report after uploading.
- [ ] Finalize the [privacy policy draft](privacy-policy.md): supply the Play developer name, real privacy contact, and publication date; verify it against the final signed bundle and any SDKs. Publish it at a public, accessible, non-editable URL (not a PDF); enter that URL in Play Console and make the policy accessible inside the app. The current build stores pins, portions, shopping selections, manually added item text, and checkmarks in app preferences; Android Auto Backup may upload these to the user's Google account when device backup is enabled. Check the Data safety answers against the final bundle and Google's definitions rather than assuming that "offline" means no data leaves the device.
- [ ] Start internal testing, then closed testing as soon as the release is stable enough for testers.

## Weeks 2–3 — tester run

If the Play Console uses a personal developer account created after November 13, 2023, production access requires **12 testers opted in continuously for at least 14 days** in the *closed* test. Internal testers alone do not satisfy this. Recruit extra people in case someone opts out. Record the start date and keep the closed test running while fixing issues.

Give each tester these tasks without explaining the screens first:

1. Find a recipe by name, then by an ingredient. Try the 30-minute filter and its empty-search recovery. Open it and follow the instructions.
2. Pin two recipes, close and reopen the app, and verify that My Pins still shows them.
3. Add one pinned recipe and one unpinned recipe to Shopping List. Change one to half and another to double; compare the amounts in the detail page and Shopping List. Try pumpkin pie and chocolate cake only after reviewing pan and bake guidance.
4. Add and check a manual shopping item. Check two recipe ingredients, close and reopen the app, and verify all checkmarks. Change the batch size and note that the recipe's checkmarks reset.
5. Unpin a shopping recipe; verify that it disappears from My Pins but remains in Shopping List with its checkmarks. Remove it explicitly from Shopping List and verify the other shopping recipes remain.
6. Use a small phone and the largest font size you normally use. Try both light and dark mode.
7. Start the cooking view, move forward and backward through steps, rotate the phone, then exit with Back. Cook and review some of the [20 newly sourced recipes](recipe-provenance.md) before launch. Verify the current step survives rotation and the full directions remain available.

Ask each tester: device model, Android version, app version, exact steps, expected result, actual result, screenshot or screen recording if possible, and whether they would use the app again. Have them report confusing behavior even if nothing crashes. Review Play Console's pre-launch report and testing feedback daily.

### Stress test matrix

| Scenario | Automated coverage | Real-device check before release |
| --- | --- | --- |
| Rapid search and tab switching | Emulator: eight cycles of matching and unmatched searches across all tabs on API 23 and API 36 | Repeat on a small or slower phone; rapidly pin and unpin different recipes while searching, then inspect My Pins and Shopping List |
| Portion and shopping changes | Unit tests check portion calculations; emulator pins Pizza, confirms Shopping stays empty until added, halves and doubles it, checks shopping reset and activity recreation, then unpins and removes it from Shopping | Change batch size repeatedly, tick shopping items, switch recipes, close and reopen; check that quantities and checkmarks match the selected batches |
| Activity and process recovery | Emulator recreates the main activity | Rotate on each tab and a detail page; background the app, force-stop it, then reopen it and verify saved pins, portions, and shopping checks |
| Cooking view | Emulator navigates steps and recreates the detail activity | Check large text, TalkBack, landscape rotation, and screen timeout while cooking; close the view and check normal sleep resumes |
| Older and constrained device | Emulator runs API 23 with default resources | Try the oldest available phone, low storage or memory pressure, and a slow connection; look for crashes, clipped controls, and delayed taps |
| Install and upgrade | CI assembles the APK and bundle | Install a signed test build, save data, install a higher `versionCode` through Play on top, and verify preferences survive |
| Accessibility and connectivity | Android lint catches some resource and accessibility issues | Use TalkBack and large text through the entire recipe, pin, portion, and shopping flow; repeat without network access |

Record every failed step with the app version and device details. Resolve any data loss or incorrect quantity before asking testers to rely on the shopping list.

## Week 4 — release gate

- [ ] All automated checks pass on the exact commit being released.
- [ ] At least two real-device smoke tests pass, including a clean install and an upgrade that keeps pins and shopping progress.
- [ ] No unresolved crash, broken navigation, missing recipe, incorrect ingredient quantity, or inaccessible primary action.
- [ ] Testers can distinguish saving a favorite from adding a recipe to Shopping List. Confirm that unpinning does not discard an active shopping list.
- [ ] Verify the 20 new recipe instructions against cooked results before production, especially scaled baked recipes and food temperatures.
- [ ] If the closed-test rule applies, apply for production access after the continuous 14-day requirement. Allow time for review; approval is not automatic.
- [ ] Generate a signed release bundle, increment `versionCode` for every later Play upload, inspect the final listing, and submit only after the checks above.
- [ ] Verify the signed bundle's package ID, version, permissions, and upload certificate fingerprint against Play Console. Keep the upload keystore and passwords in separate secure backups; do not put them in the repository or CI logs. Install and upgrade using the Play testing track, because CI's `bundleRelease` is an unsigned build artifact.

Sources: [Play Console account setup](https://support.google.com/googleplay/android-developer/answer/6112435), [Google Play target API requirements](https://support.google.com/googleplay/android-developer/answer/11926878), [closed testing requirements](https://support.google.com/googleplay/android-developer/answer/14151465), [Google Play User Data policy](https://support.google.com/googleplay/android-developer/answer/10144311), [Android Auto Backup](https://developer.android.com/identity/data/autobackup), [Android app signing](https://developer.android.com/studio/publish/app-signing).
