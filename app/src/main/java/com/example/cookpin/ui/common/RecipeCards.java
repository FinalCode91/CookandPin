package com.example.cookpin.ui.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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

public final class RecipeCards {
    public static View create(Context context, RecipeCatalog.Recipe recipe, Runnable onPinChanged) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        int space = (int) (context.getResources().getDisplayMetrics().density * 12);
        card.setPadding(space, space, space, space);
        card.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        TypedArray touchStyle = context.obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
        card.setForeground(touchStyle.getDrawable(0));
        touchStyle.recycle();
        ImageView image = new ImageView(context);
        image.setImageResource(recipe.image);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        int size = space * 6;
        card.addView(image, new LinearLayout.LayoutParams(size, size));

        LinearLayout info = new LinearLayout(context);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(space, 0, space / 2, 0);
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
        card.addView(info, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button pin = new Button(context);
        pin.setText(PinnedRecipes.contains(context, recipe.id) ? R.string.unpin_short : R.string.pin_short);
        pin.setOnClickListener(v -> {
            boolean pinned = !PinnedRecipes.contains(context, recipe.id);
            PinnedRecipes.setPinned(context, recipe.id, pinned);
            pin.setText(pinned ? R.string.unpin_short : R.string.pin_short);
            if (onPinChanged != null) onPinChanged.run();
        });
        card.addView(pin, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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

    private RecipeCards() {}
}
