package com.example.cookpin.ui.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.cookpin.R;
import com.example.cookpin.RecipeDetailActivity;
import com.example.cookpin.data.PinnedRecipes;
import com.example.cookpin.data.RecipeCatalog;
import com.google.android.material.card.MaterialCardView;

public final class RecipeCards {
    public static View create(Context context, RecipeCatalog.Recipe recipe, Runnable onPinChanged) {
        int space = (int) (context.getResources().getDisplayMetrics().density * 12);
        MaterialCardView card = new MaterialCardView(context);
        card.setRadius(space);
        card.setCardElevation(space / 6f);
        card.setUseCompatPadding(true);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.bottomMargin = space / 2;
        card.setLayoutParams(cardParams);

        LinearLayout body = new LinearLayout(context);
        body.setOrientation(LinearLayout.VERTICAL);
        ImageView image = new ImageView(context);
        if (recipe.image == R.drawable.ic_recipe_placeholder) image.setImageResource(recipe.image);
        else image.setImageBitmap(RecipeImages.get(context.getResources(), recipe.image));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        body.addView(image, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, space * 12));

        LinearLayout row = new LinearLayout(context);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(space, space / 2, space, space / 2);
        LinearLayout info = new LinearLayout(context);
        info.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(context);
        title.setText(recipe.title);
        title.setTextSize(18);
        title.setTypeface(null, Typeface.BOLD);
        title.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        info.addView(title);
        TextView time = new TextView(context);
        time.setText(context.getString(R.string.estimated_time, recipe.minutes));
        time.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        info.addView(time);
        row.addView(info, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button pin = new Button(context);
        updatePin(context, pin, recipe);
        pin.setOnClickListener(v -> {
            PinnedRecipes.setPinned(context, recipe.id, !PinnedRecipes.contains(context, recipe.id));
            updatePin(context, pin, recipe);
            if (onPinChanged != null) onPinChanged.run();
        });
        row.addView(pin, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        body.addView(row);
        card.addView(body);
        card.setClickable(true);
        card.setFocusable(true);
        card.setContentDescription(context.getString(R.string.open_recipe_with_time, recipe.title, recipe.minutes));
        card.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("recipeId", recipe.id);
            context.startActivity(intent);
        });
        return card;
    }

    private static void updatePin(Context context, Button pin, RecipeCatalog.Recipe recipe) {
        boolean pinned = PinnedRecipes.contains(context, recipe.id);
        pin.setText(pinned ? R.string.unpin_short : R.string.pin_short);
        pin.setContentDescription(context.getString(
                pinned ? R.string.unpin_named_recipe : R.string.pin_named_recipe, recipe.title));
    }

    private RecipeCards() {}
}
