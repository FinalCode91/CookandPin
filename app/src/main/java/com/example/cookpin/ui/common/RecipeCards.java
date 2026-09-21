package com.example.cookpin.ui.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.cookpin.R;
import com.example.cookpin.RecipeDetailActivity;
import com.example.cookpin.data.PinnedRecipes;
import com.example.cookpin.data.RecipeCatalog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;

public final class RecipeCards {
    public static View create(Context context, RecipeCatalog.Recipe recipe, Runnable onPinChanged) {
        float density = context.getResources().getDisplayMetrics().density;
        int space = (int) (density * 16);
        MaterialCardView card = new MaterialCardView(context);
        card.setRadius(16 * density);
        card.setCardElevation(1 * density);
        card.setStrokeWidth((int) density);
        card.setStrokeColor(context.getColor(R.color.divider));
        card.setCardBackgroundColor(context.getColor(R.color.surface));
        card.setUseCompatPadding(false);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.bottomMargin = (int) (12 * density);
        card.setLayoutParams(cardParams);

        LinearLayout body = new LinearLayout(context);
        body.setOrientation(LinearLayout.VERTICAL);
        ImageView image = new ImageView(context);
        if (recipe.image == R.drawable.ic_recipe_placeholder) image.setImageResource(recipe.image);
        else image.setImageBitmap(RecipeImages.get(context.getResources(), recipe.image));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setBackgroundColor(context.getColor(R.color.surface_variant));
        image.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        body.addView(image, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int) (164 * density)));

        LinearLayout row = new LinearLayout(context);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(space, (int) (12 * density), (int) (12 * density), (int) (12 * density));
        LinearLayout info = new LinearLayout(context);
        info.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(context);
        title.setText(recipe.title);
        title.setTextSize(18);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(context.getColor(R.color.on_surface));
        title.setMaxLines(2);
        title.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        info.addView(title);
        TextView time = new TextView(context);
        time.setText(context.getString(R.string.estimated_time, recipe.minutes));
        time.setTextSize(14);
        time.setTextColor(context.getColor(R.color.on_surface_variant));
        time.setPadding(0, (int) (4 * density), 0, 0);
        time.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        info.addView(time);
        row.addView(info, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        MaterialButton pin = new MaterialButton(
                context, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
        pin.setMinHeight((int) (48 * density));
        pin.setInsetTop(0);
        pin.setInsetBottom(0);
        pin.setCornerRadius((int) (12 * density));
        pin.setStrokeWidth((int) density);
        pin.setStrokeColor(ColorStateList.valueOf(context.getColor(R.color.outline)));
        pin.setTextColor(context.getColor(R.color.brand_brown));
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

    private static void updatePin(Context context, MaterialButton pin, RecipeCatalog.Recipe recipe) {
        boolean pinned = PinnedRecipes.contains(context, recipe.id);
        pin.setText(pinned ? R.string.unpin_short : R.string.pin_short);
        pin.setBackgroundTintList(ColorStateList.valueOf(context.getColor(
                pinned ? R.color.surface_variant : R.color.surface)));
        pin.setContentDescription(context.getString(
                pinned ? R.string.unpin_named_recipe : R.string.pin_named_recipe, recipe.title));
    }

    private RecipeCards() {}
}
