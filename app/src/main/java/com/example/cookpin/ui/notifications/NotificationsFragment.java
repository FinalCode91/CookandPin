package com.example.cookpin.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cookpin.R;
import com.example.cookpin.data.PinnedRecipes;
import com.example.cookpin.data.RecipeCatalog;

public class NotificationsFragment extends Fragment {
    private LinearLayout items;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        items = root.findViewById(R.id.shoppingItems);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (items == null) return;
        items.removeAllViews();
        SharedPreferences checked = requireContext().getSharedPreferences("cookandpin_shopping", Context.MODE_PRIVATE);
        int recipeCount = 0;
        for (RecipeCatalog.Recipe recipe : RecipeCatalog.ALL) {
            if (!PinnedRecipes.contains(requireContext(), recipe.id)) continue;
            recipeCount++;
            TextView heading = new TextView(requireContext());
            heading.setText(recipe.title);
            heading.setTextSize(20);
            heading.setPadding(0, 18, 0, 8);
            items.addView(heading);
            for (String ingredient : recipe.ingredients.split("\n")) {
                String key = recipe.id + ":" + ingredient;
                CheckBox box = new CheckBox(requireContext());
                box.setText(ingredient);
                box.setTextSize(17);
                box.setPadding(0, 4, 0, 4);
                box.setChecked(checked.getBoolean(key, false));
                box.setOnCheckedChangeListener((button, value) -> checked.edit().putBoolean(key, value).apply());
                items.addView(box);
            }
        }
        if (recipeCount == 0) {
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
    }
}
