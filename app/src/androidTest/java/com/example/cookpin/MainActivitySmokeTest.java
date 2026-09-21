package com.example.cookpin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
}
