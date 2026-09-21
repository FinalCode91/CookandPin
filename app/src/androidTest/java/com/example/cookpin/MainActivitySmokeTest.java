package com.example.cookpin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivitySmokeTest {
    @Rule public ActivityScenarioRule<MainActivity> activity =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test public void repeatedSearchNavigationAndRecreationStayUsable() {
        for (int i = 0; i < 8; i++) {
            onView(withId(R.id.recipeSearch)).perform(replaceText("pizza"));
            onView(withId(R.id.recipeResults)).check(matches(hasMinimumChildCount(1)));
            onView(withText(R.string.recipe1_description)).check(matches(isDisplayed()));
            onView(withId(R.id.recipeSearch)).perform(replaceText("no-recipe-matches-999"));
            onView(withText(R.string.no_search_results)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_dashboard)).perform(click());
            onView(withId(R.id.savedRecipes)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_notifications)).perform(click());
            onView(withId(R.id.shoppingItems)).check(matches(isDisplayed()));
            onView(withId(R.id.navigation_home)).perform(click());
        }

        // Recreate the activity to cover the same lifecycle path as a rotation.
        activity.getScenario().recreate();
        onView(withId(R.id.recipeSearch)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeSearch)).perform(replaceText("pizza"));
        onView(withId(R.id.recipeResults)).check(matches(hasMinimumChildCount(1)));
        onView(withText(R.string.recipe1_description)).check(matches(isDisplayed()));
    }

    @Test public void pinScaleAndShoppingCheckPersistAcrossActivityRecreation() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        for (String name : new String[] {"cookandpin_pins", "cookandpin_portions", "cookandpin_shopping"}) {
            context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
        }
        activity.getScenario().recreate();

        onView(withContentDescription("Pin Pizza")).perform(click());
        onView(withId(R.id.navigation_dashboard)).perform(click());
        onView(withContentDescription(startsWith("Open Pizza recipe"))).check(matches(isDisplayed()));
        onView(withId(R.id.navigation_notifications)).perform(click());
        onView(withText("2 1/2 cups all-purpose flour")).perform(click());
        onView(withText("2 1/2 cups all-purpose flour")).check(matches(isChecked()));

        onView(withId(R.id.navigation_home)).perform(click());
        onView(withContentDescription(startsWith("Open Pizza recipe"))).perform(click());
        onView(withId(R.id.decreasePortions)).perform(scrollTo(), click());
        onView(withId(R.id.portionSummary)).check(matches(withText(containsString("half batch"))));
        onView(withId(R.id.increasePortions)).perform(scrollTo(), click(), click(), click());
        onView(withId(R.id.portionSummary)).check(matches(withText(containsString("double batch"))));
        pressBack();

        onView(withId(R.id.navigation_notifications)).perform(click());
        onView(withText("5 cups all-purpose flour")).check(matches(isNotChecked()));
        onView(withText("5 cups all-purpose flour")).perform(click());
        activity.getScenario().recreate();
        onView(withText("5 cups all-purpose flour")).check(matches(isChecked()));

        onView(withId(R.id.navigation_dashboard)).perform(click());
        onView(withContentDescription("Unpin Pizza")).perform(click());
        onView(withId(R.id.navigation_notifications)).perform(click());
        onView(withText(R.string.no_shopping_items)).check(matches(isDisplayed()));
    }
}
