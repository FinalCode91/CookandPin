package com.example.cookpin.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import com.example.cookpin.R;
import com.example.cookpin.data.PortionPreferences;
import com.example.cookpin.data.ManualShoppingItems;
import com.example.cookpin.data.PortionScaler;
import com.example.cookpin.data.RecipeCatalog;
import com.example.cookpin.data.ShoppingRecipes;

public class NotificationsFragment extends Fragment {
    private LinearLayout items;
    private EditText newItem;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        items = root.findViewById(R.id.shoppingItems);
        newItem = root.findViewById(R.id.newShoppingItem);
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
        SharedPreferences checked = requireContext().getSharedPreferences("cookandpin_shopping", Context.MODE_PRIVATE);
        java.util.List<ManualShoppingItems.Item> manualItems = ManualShoppingItems.get(requireContext());
        if (!manualItems.isEmpty()) {
            TextView heading = new TextView(requireContext());
            heading.setText(R.string.manual_shopping_heading);
            heading.setTextSize(20);
            ViewCompat.setAccessibilityHeading(heading, true);
            heading.setPadding(0, 18, 0, 8);
            items.addView(heading);
            for (ManualShoppingItems.Item item : manualItems) {
                LinearLayout row = new LinearLayout(requireContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(android.view.Gravity.CENTER_VERTICAL);
                CheckBox box = new CheckBox(requireContext());
                box.setText(item.name);
                box.setTextSize(17);
                box.setChecked(item.checked);
                box.setOnCheckedChangeListener((button, value) ->
                        ManualShoppingItems.setChecked(requireContext(), item.id, value));
                row.addView(box, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                Button remove = new Button(requireContext());
                remove.setText(R.string.remove_shopping_item);
                remove.setContentDescription(getString(R.string.remove_shopping_item_named, item.name));
                remove.setOnClickListener(v -> {
                    ManualShoppingItems.remove(requireContext(), item.id);
                    render();
                });
                row.addView(remove);
                items.addView(row);
            }
        }
        int recipeCount = 0;
        for (RecipeCatalog.Recipe recipe : RecipeCatalog.ALL) {
            if (!ShoppingRecipes.contains(requireContext(), recipe.id)) continue;
            recipeCount++;
            TextView heading = new TextView(requireContext());
            heading.setText(getString(R.string.shopping_recipe_heading, recipe.title,
                    PortionScaler.servings(recipe, PortionPreferences.get(requireContext(), recipe.id))));
            heading.setTextSize(20);
            ViewCompat.setAccessibilityHeading(heading, true);
            heading.setPadding(0, 18, 0, 8);
            items.addView(heading);
            Button remove = new Button(requireContext());
            remove.setText(R.string.remove_from_shopping);
            remove.setContentDescription(getString(R.string.remove_from_list_named, recipe.title));
            remove.setOnClickListener(v -> {
                ShoppingRecipes.setSelected(requireContext(), recipe.id, false);
                render();
            });
            items.addView(remove);
            for (String ingredient : recipe.ingredients.split("\n")) {
                String key = recipe.id + ":" + ingredient;
                CheckBox box = new CheckBox(requireContext());
                box.setText(PortionScaler.ingredient(ingredient,
                        PortionPreferences.get(requireContext(), recipe.id)));
                box.setTextSize(17);
                box.setPadding(0, 4, 0, 4);
                box.setChecked(checked.getBoolean(key, false));
                box.setOnCheckedChangeListener((button, value) -> checked.edit().putBoolean(key, value).apply());
                items.addView(box);
            }
        }
        if (recipeCount == 0 && manualItems.isEmpty()) {
            TextView empty = new TextView(requireContext());
            empty.setText(R.string.no_shopping_items);
            empty.setTextSize(18);
            items.addView(empty);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        items = null;
        newItem = null;
    }
}
