package com.example.cookpin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DiscoveryFilterTest {
    @Rule public ActivityScenarioRule<MainActivity> activity =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test public void timeFilterAndEmptyStateRecoverAfterRecreation() {
        onView(withId(R.id.quickRecipes)).perform(click());
        onView(withId(R.id.recipeResults)).check(matches(hasMinimumChildCount(2)));
        onView(withId(R.id.recipeSearch)).perform(replaceText("pizza"));
        onView(withText(R.string.no_filtered_results)).check(matches(isDisplayed()));

        activity.getScenario().recreate();
        onView(withId(R.id.quickRecipes)).check(matches(isChecked()));
        onView(withId(R.id.recipeSearch)).check(matches(withText("pizza")));
        onView(withText(R.string.no_filtered_results)).check(matches(isDisplayed()));

        onView(withText(R.string.clear_search)).perform(click());
        onView(withId(R.id.recipeSearch)).check(matches(withText("")));
        onView(withId(R.id.recipeResults)).check(matches(hasMinimumChildCount(2)));
        onView(withId(R.id.quickRecipes)).perform(click());
        onView(withId(R.id.recipeResults)).check(matches(hasMinimumChildCount(29)));
    }

    @Test public void ingredientSearchWithinQuickRecipes() {
        onView(withId(R.id.quickRecipes)).perform(click());
        onView(withId(R.id.recipeSearch)).perform(replaceText("broccoli"));
        onView(withId(R.id.recipeResults)).check(matches(hasMinimumChildCount(2)));
        onView(withText(R.string.recipe6_description)).check(matches(isDisplayed()));
        onView(withText("Vegetable Coconut Curry")).perform(scrollTo())
                .check(matches(isDisplayed()));
    }
}
