package com.example.cookpin.data;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public final class PinnedRecipes {
    private static final String PREFS = "cookandpin_pins";
    private static final String IDS = "recipe_ids";

    public static boolean contains(Context context, String id) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getStringSet(IDS, java.util.Collections.emptySet()).contains(id);
    }

    public static void setPinned(Context context, String id, boolean pinned) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Set<String> ids = new HashSet<>(preferences.getStringSet(IDS, java.util.Collections.emptySet()));
        if (pinned) ids.add(id);
        else {
            ids.remove(id);
            RecipeCatalog.Recipe recipe = RecipeCatalog.find(id);
            if (recipe != null) {
                SharedPreferences.Editor shopping = context.getSharedPreferences(
                        "cookandpin_shopping", Context.MODE_PRIVATE).edit();
                for (String ingredient : recipe.ingredients.split("\n")) {
                    shopping.remove(id + ":" + ingredient);
                }
                shopping.apply();
            }
        }
        preferences.edit().putStringSet(IDS, ids).apply();
    }

    private PinnedRecipes() {}
}
