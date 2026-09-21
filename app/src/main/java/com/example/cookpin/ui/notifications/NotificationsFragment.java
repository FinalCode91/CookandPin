package com.example.cookpin.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import com.example.cookpin.R;
import com.example.cookpin.data.ManualShoppingItems;
import com.example.cookpin.data.PortionPreferences;
import com.example.cookpin.data.PortionScaler;
import com.example.cookpin.data.RecipeCatalog;
import com.example.cookpin.data.ShoppingRecipes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class NotificationsFragment extends Fragment {
    private LinearLayout items;
    private EditText newItem;
    private TextView summary;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        items = root.findViewById(R.id.shoppingItems);
        newItem = root.findViewById(R.id.newShoppingItem);
        summary = root.findViewById(R.id.shoppingSummary);
        root.findViewById(R.id.addShoppingItem).setOnClickListener(v -> addItem());
        newItem.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_DONE) return false;
            addItem();
            return true;
        });
        return root;
    }

    private void addItem() {
        String name = newItem.getText().toString().trim();
        if (name.isEmpty()) {
            newItem.setError(getString(R.string.shopping_item_required));
            return;
        }
        ManualShoppingItems.add(requireContext(), name);
        newItem.setText("");
        render();
    }

    @Override
    public void onResume() {
        super.onResume();
        render();
    }

    private void render() {
        if (items == null || getContext() == null) return;
        items.removeAllViews();
        SharedPreferences checked = requireContext()
                .getSharedPreferences("cookandpin_shopping", Context.MODE_PRIVATE);
        java.util.List<ManualShoppingItems.Item> manualItems = ManualShoppingItems.get(requireContext());
        int recipeCount = 0;
        int itemCount = manualItems.size();

        if (!manualItems.isEmpty()) {
            LinearLayout card = addSectionCard();
            card.addView(sectionHeading(getString(R.string.manual_shopping_heading)));
            for (ManualShoppingItems.Item item : manualItems) {
                LinearLayout row = new LinearLayout(requireContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setPadding(0, dp(2), 0, dp(2));

                CheckBox box = new CheckBox(requireContext());
                box.setText(item.name);
                box.setTextSize(16);
                box.setTextColor(requireContext().getColor(R.color.on_surface));
                box.setChecked(item.checked);
                updateCheckedStyle(box, item.checked);
                box.setOnCheckedChangeListener((button, value) -> {
                    ManualShoppingItems.setChecked(requireContext(), item.id, value);
                    updateCheckedStyle(button, value);
                });
                row.addView(box, new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                MaterialButton remove = textButton();
                remove.setText(R.string.remove_shopping_item);
                remove.setContentDescription(getString(R.string.remove_shopping_item_named, item.name));
                remove.setOnClickListener(v -> {
                    ManualShoppingItems.remove(requireContext(), item.id);
                    render();
                });
                row.addView(remove, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, dp(48)));
                card.addView(row);
            }
        }

        for (RecipeCatalog.Recipe recipe : RecipeCatalog.ALL) {
            if (!ShoppingRecipes.contains(requireContext(), recipe.id)) continue;
            recipeCount++;
            int scale = PortionPreferences.get(requireContext(), recipe.id);
            String[] ingredients = recipe.ingredients.split("\n");
            itemCount += ingredients.length;

            LinearLayout card = addSectionCard();
            TextView heading = sectionHeading(getString(R.string.shopping_recipe_heading,
                    recipe.title, PortionScaler.servings(recipe, scale)));
            card.addView(heading);

            MaterialButton remove = textButton();
            remove.setText(R.string.remove_recipe_from_list);
            remove.setContentDescription(getString(R.string.remove_from_list_named, recipe.title));
            remove.setOnClickListener(v -> {
                ShoppingRecipes.setSelected(requireContext(), recipe.id, false);
                render();
            });
            card.addView(remove, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, dp(48)));

            for (String ingredient : ingredients) {
                String key = recipe.id + ":" + ingredient;
                CheckBox box = new CheckBox(requireContext());
                box.setText(PortionScaler.ingredient(ingredient, scale));
                box.setTextSize(16);
                box.setTextColor(requireContext().getColor(R.color.on_surface));
                box.setPadding(0, dp(2), 0, dp(2));
                box.setChecked(checked.getBoolean(key, false));
                updateCheckedStyle(box, box.isChecked());
                box.setOnCheckedChangeListener((button, value) -> {
                    checked.edit().putBoolean(key, value).apply();
                    updateCheckedStyle(button, value);
                });
                card.addView(box);
            }
        }

        if (summary != null) {
            summary.setText(itemCount == 0
                    ? getString(R.string.shopping_supporting)
                    : getString(R.string.shopping_count, itemCount, recipeCount));
        }

        if (recipeCount == 0 && manualItems.isEmpty()) {
            TextView empty = new TextView(requireContext());
            empty.setText(R.string.no_shopping_items);
            empty.setTextSize(18);
            empty.setTextColor(requireContext().getColor(R.color.on_surface_variant));
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(dp(24), dp(72), dp(24), dp(24));
            items.addView(empty);
        }
    }

    private LinearLayout addSectionCard() {
        MaterialCardView outer = new MaterialCardView(requireContext());
        outer.setRadius(dp(16));
        outer.setCardElevation(dp(1));
        outer.setStrokeWidth(dp(1));
        outer.setStrokeColor(requireContext().getColor(R.color.divider));
        outer.setCardBackgroundColor(requireContext().getColor(R.color.surface));
        LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        outerParams.bottomMargin = dp(12);
        outer.setLayoutParams(outerParams);

        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(14), dp(16), dp(14));
        outer.addView(card);
        items.addView(outer);
        return card;
    }

    private TextView sectionHeading(String text) {
        TextView heading = new TextView(requireContext());
        heading.setText(text);
        heading.setTextSize(19);
        heading.setTextColor(requireContext().getColor(R.color.on_surface));
        heading.setTypeface(null, android.graphics.Typeface.BOLD);
        heading.setPadding(0, 0, 0, dp(6));
        ViewCompat.setAccessibilityHeading(heading, true);
        return heading;
    }

    private MaterialButton textButton() {
        MaterialButton button = new MaterialButton(
                requireContext(), null, com.google.android.material.R.attr.materialButtonStyle);
        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                requireContext().getColor(R.color.surface_variant)));
        button.setTextColor(requireContext().getColor(R.color.brand_brown));
        button.setCornerRadius(dp(10));
        button.setInsetTop(0);
        button.setInsetBottom(0);
        button.setAllCaps(false);
        return button;
    }

    private void updateCheckedStyle(android.widget.CompoundButton box, boolean checked) {
        if (!(box instanceof TextView)) return;
        TextView text = (TextView) box;
        int flags = text.getPaintFlags();
        text.setPaintFlags(checked
                ? flags | Paint.STRIKE_THRU_TEXT_FLAG
                : flags & ~Paint.STRIKE_THRU_TEXT_FLAG);
        text.setAlpha(checked ? 0.58f : 1f);
    }

    private int dp(int value) {
        return Math.round(value * requireContext().getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        items = null;
        newItem = null;
        summary = null;
    }
}
