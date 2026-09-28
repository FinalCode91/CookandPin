package com.example.cookpin.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cookpin.R;
import com.example.cookpin.data.PinnedRecipes;
import com.example.cookpin.data.RecipeCatalog;
import com.example.cookpin.ui.common.RecipeCards;

public class DashboardFragment extends Fragment {
    private LinearLayout results;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        results = root.findViewById(R.id.savedRecipes);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        render();
    }

    private void render() {
        if (results == null) return;
        results.removeAllViews();
        int count = 0;
        for (RecipeCatalog.Recipe recipe : RecipeCatalog.ALL) {
            if (!PinnedRecipes.contains(requireContext(), recipe.id)) continue;
            results.addView(RecipeCards.create(requireContext(), recipe, this::render));
            count++;
        }
        if (count == 0) {
            TextView empty = new TextView(requireContext());
            empty.setText(R.string.no_pins);
            empty.setTextSize(18);
            empty.setTextColor(requireContext().getColor(R.color.on_surface_variant));
            empty.setGravity(android.view.Gravity.CENTER);
            empty.setPadding(24, 72, 24, 24);
            results.addView(empty);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        results = null;
    }
}
