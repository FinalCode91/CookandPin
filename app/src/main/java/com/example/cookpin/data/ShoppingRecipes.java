package com.example.cookpin.data;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Recipe selection for shopping, independent of saved favorites. */
public final class ShoppingRecipes {
    private static final String PREFS = "cookandpin_shopping_recipes";
    private static final String IDS = "recipe_ids";
    private static final String INITIALIZED = "initialized";

    /** Existing favorites were the shopping list in older versions. Keep them on upgrade. */
    public static void initialize(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        if (preferences.getBoolean(INITIALIZED, false)) return;
        Set<String> oldPins = context.getSharedPreferences("cookandpin_pins", Context.MODE_PRIVATE)
                .getStringSet(IDS, Collections.emptySet());
        preferences.edit().putStringSet(IDS, new HashSet<>(oldPins))
                .putBoolean(INITIALIZED, true).commit();
    }

    public static boolean contains(Context context, String id) {
        initialize(context);
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getStringSet(IDS, Collections.emptySet()).contains(id);
    }

    public static void setSelected(Context context, String id, boolean selected) {
        initialize(context);
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Set<String> ids = new HashSet<>(preferences.getStringSet(IDS, Collections.emptySet()));
        if (selected) ids.add(id);
        else ids.remove(id);
        preferences.edit().putStringSet(IDS, ids).apply();
        if (!selected) {
            RecipeCatalog.Recipe recipe = RecipeCatalog.find(id);
            if (recipe != null) {
                SharedPreferences.Editor checked = context.getSharedPreferences(
                        "cookandpin_shopping", Context.MODE_PRIVATE).edit();
                for (String ingredient : recipe.ingredients.split("\n")) {
                    checked.remove(id + ":" + ingredient);
                }
                checked.apply();
            }
        }
    }

    private ShoppingRecipes() {}
}
