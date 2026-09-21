package com.example.cookpin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** A few real emulator frames for a short product walkthrough. */
@RunWith(AndroidJUnit4.class)
public class DemoScreenshotsTest {
    @Rule public ActivityScenarioRule<MainActivity> activity =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test public void captureWalkthroughOnAndroid16() throws IOException {
        if (Build.VERSION.SDK_INT < 36) return;
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        for (String pref : new String[] {"cookandpin_pins", "cookandpin_portions",
                "cookandpin_shopping", "cookandpin_shopping_recipes"}) {
            context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit().clear().commit();
        }
        activity.getScenario().recreate();
        capture("01-discover.png");
        onView(withId(R.id.recipeSearch)).perform(replaceText("chili"), closeSoftKeyboard());
        capture("02-search.png");
        onView(withContentDescription(startsWith("Open Vegetarian Three-Bean Chili recipe")))
                .perform(scrollTo(), click());
        capture("03-recipe.png");
        onView(withId(R.id.increasePortions)).perform(scrollTo(), click());
        capture("04-portions.png");
        onView(withId(R.id.startCooking)).perform(scrollTo(), click());
        capture("05-cooking.png");
        onView(withId(R.id.closeCooking)).perform(click());
        onView(withId(R.id.shoppingRecipe)).perform(scrollTo(), click());
        pressBack();
        onView(withId(R.id.navigation_notifications)).perform(click());
        capture("06-shopping.png");
        onView(withId(R.id.newShoppingItem)).perform(replaceText("Oat milk"), closeSoftKeyboard());
        onView(withId(R.id.addShoppingItem)).perform(click());
        capture("07-manual-item.png");
    }

    private static void capture(String name) throws IOException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CookandPinDemo");
        Uri uri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        assertTrue("Could not create: " + name, uri != null);
        Bitmap bitmap = InstrumentationRegistry.getInstrumentation()
                .getUiAutomation().takeScreenshot();
        assertTrue("Screenshot unavailable: " + name, bitmap != null);
        try (OutputStream output = context.getContentResolver().openOutputStream(uri)) {
            assertTrue("Could not open: " + name, output != null);
            assertTrue("Could not save: " + name, bitmap.compress(Bitmap.CompressFormat.PNG, 100, output));
        } finally {
            bitmap.recycle();
        }
    }
}
