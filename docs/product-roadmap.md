# cookandpin: four-week launch roadmap

Planning date: September 21, 2026. Target: a reliable first Google Play release around October 21. The exact production date depends on account setup, testing, and Google's review. This roadmap complements the [release and stress-test checklist](launch-checklist.md).

## Product position and current baseline

cookandpin is a small offline cookbook: nine bundled recipes, title and ingredient search, saved pins, estimated half/original/1.5×/double portions, and a shopping list with persistent checkmarks. Recipes can now be selected for Shopping independently of pins, and older users' pinned shopping lists migrate. Shopping quantities remain grouped by recipe. No account, import, meal calendar, nutrition database, or sync is present.

Competitors show the baseline users may expect. [Paprika](https://play.google.com/store/apps/details?id=com.hindsightlabs.paprika.android.v3) lists recipe import, scaling, sync, meal plans, and a cooking view. [Mealime](https://play.google.com/store/apps/details?id=com.mealime) offers personalized meal plans and an automatic grocery list. [AnyList](https://play.google.com/store/apps/details?id=com.purplecover.anylist) offers recipe import, a meal calendar, and one-tap shopping additions. [Samsung Food](https://play.google.com/store/apps/details?id=com.foodient.whisk) offers a large recipe library, dietary search, planning, and shared lists. [Recipe Keeper](https://play.google.com/store/apps/details?id=com.tudorspan.recipekeeper) includes offline access, scaling, and aisle grouping. These are product-page descriptions, not controlled comparisons or evidence that any app performs better in a particular test.

The immediate opportunity is a dependable, uncluttered offline path from a recipe to correctly scaled ingredients and a usable store list. Nine recipes limit repeat use; more verified content matters, but quality and rights come before a numeric recipe target. Do not promise that a first release exceeds established apps.

## Priority order

| Priority | Work and acceptance criterion | Decision |
| --- | --- | --- |
| P0 | Play setup: owner checks whether a developer account exists, completes registration and verification if needed, uses the agreed package ID, secures an upload key, verifies photo and icon rights, and completes required listing declarations. | Required before release; begin immediately. |
| P0 | Core reliability: searches, pins, independent shopping selection, checkmarks, and portion estimates survive restart and upgrade; no known crash, missing recipe, incorrect quantity, or inaccessible primary action. | Release gate. |
| P0 | Real-device and Play-track validation: exact signed release bundle, clean install and upgrade, small/large text, TalkBack, offline use, and low-end phone where possible; triage tester feedback and Play pre-launch issues. | Release gate. |
| P1 | Focused cooking view: readable step-by-step layout, clear current step, and optional keep-awake behavior while actively cooking. Preserve a straightforward return to the full recipe. | Build in week 2 only if P0 remains green; test on a real phone. |
| P1 | Improve recipe coverage and discovery: add only recipes with tested ingredient amounts, directions, appropriate scaling guidance, and cleared images; add useful filters only where catalog metadata supports them. | Stage after quality review; do not delay account setup or closed-test start. |
| P2 | Combine duplicate grocery items across recipes, aisle grouping, custom recipes or imports, sync, family sharing, planning calendar, nutrition, and retailer integration. | After first release and user feedback. Text-only ingredient lines do not safely support quantity merging. |

## Week 1: unlock the critical path

1. Owner signs in at [Play Console](https://play.google.com/console/) and checks account status. If no account exists, create one and complete the requested identity/device checks promptly. Use `cookandpin` as the visible name and `com.finalcode91.cookandpin` as the technical Android application ID for the first upload.
2. Clear usage rights for all nine recipe images and launcher art. If rights cannot be established, replace assets before public distribution. Confirm recipe source and actually review ingredient units, temperatures, and scaling, especially baked goods.
3. Run CI for unit tests, lint, APK, release bundle, and emulator smoke scenarios. Install the app on at least one physical device, record defects, and fix P0 issues.
4. Prepare listing text, screenshots from the actual app, contact details, content rating, Data safety declaration, and privately backed-up signing credentials. Upload an internally tested signed bundle.
5. Recruit more than 12 potential closed testers and schedule feedback. Start the closed test as soon as app setup and a sufficiently stable build allow; the 14-day clock should not wait for cosmetic work.

## Week 2: test the whole workflow

1. Have testers discover a recipe, set portions, add it to Shopping without pinning, mark items, force-stop and reopen, then remove it. Repeat with a pinned recipe and an upgrade from a previous build to verify migration.
2. Stress repeated search, tab changes, rotation, half-to-double adjustments, checkmark resets, low memory, large text, dark mode, TalkBack, and offline use. Log device, OS, build, steps, expected and actual result.
3. If the P0 scenarios pass, build and test the focused cooking view. Keep it out of the release if it introduces navigation, accessibility, or data-persistence defects.
4. Gather qualitative feedback: where users hesitate, whether Shopping and Pins are understood, whether serving estimates are trusted, and whether users want to cook a second recipe. Fix recurring friction before adding breadth.

## Week 3: close defects and test release candidates

1. Prioritize reproducible crashes, incorrect food quantities or instructions, data loss, and inaccessible controls. Rerun their exact reproduction steps on the fixed build and on the last supported Android version when possible.
2. Add vetted recipe content only when ingredient amounts, steps, rights, and scaling behavior are reviewed. Reject synthetic nutrition and unverified dietary/allergen claims.
3. Test a signed release candidate through Play on at least two physical phones, including clean install and upgrade preserving pins, portions, shopping selections, and checkmarks. Review Play's pre-launch report.
4. Keep the required closed-test cohort continuously opted in where the new-personal-account rule applies. Record tester feedback and fixes for the production-access application.

## Week 4: release decision

Ship only when the [launch checklist](launch-checklist.md) gates pass on the exact version submitted. There must be no unresolved P0 defects, the rights and listing declarations must be settled, CI and real-device testing must pass, and the signed Play build must be the version tested. If a new personal account requires it, submit the production-access application only after at least 12 testers have remained opted into closed testing continuously for 14 days. Google review and approval are external dependencies; move the publication date if they or a release gate remain unresolved.

After release, prioritize improvements from observed tester and user behavior: broader trustworthy recipes, a measured cook mode iteration, and more flexible shopping. Treat imports, sync, planning, and quantity aggregation as separate projects with data-model, privacy, and regression work.

## Progress measures

Track daily during the test: active testers and uninterrupted opt-in period, verified devices and Android versions, completed core-flow scenarios, crashes and P0/P1 defect counts, and median time or hesitation points from recipe search to a usable shopping list. Ask whether each tester would use it for another meal and why. These measures inform improvements; they do not establish market leadership.

## External requirements

[Google Play's new-personal-account test policy](https://support.google.com/googleplay/android-developer/answer/14151465) requires at least 12 continuously opted-in closed testers for the preceding 14 days before applying for production access. [Current target API policy](https://support.google.com/googleplay/android-developer/answer/11926878) requires API 36 or higher for new phone apps; the project currently targets API 36. Verify current Play Console requirements again at submission.
