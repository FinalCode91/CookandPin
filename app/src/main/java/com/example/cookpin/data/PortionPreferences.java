package com.example.cookpin.data;

import android.content.Context;
import android.content.SharedPreferences;

public final class PortionPreferences {
    private static final String PREFS = "cookandpin_portions";

    public static int get(Context context, String recipeId) {
        return PortionScaler.clamp(context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getInt(recipeId, PortionScaler.ORIGINAL));
    }

    public static void set(Context context, RecipeCatalog.Recipe recipe, int scale) {
        int next = PortionScaler.clamp(scale);
        if (next == get(context, recipe.id)) return;
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putInt(recipe.id, next).apply();
        SharedPreferences.Editor shopping = context.getSharedPreferences(
                "cookandpin_shopping", Context.MODE_PRIVATE).edit();
        for (String ingredient : recipe.ingredients.split("\\n")) {
            shopping.remove(recipe.id + ":" + ingredient);
        }
        shopping.apply();
    }

    private PortionPreferences() {}
}
