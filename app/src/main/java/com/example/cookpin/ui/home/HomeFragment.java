package com.example.cookpin.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cookpin.R;
import com.example.cookpin.data.RecipeCatalog;
import com.example.cookpin.ui.common.RecipeCards;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private LinearLayout results;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        results = root.findViewById(R.id.recipeResults);
        EditText search = root.findViewById(R.id.recipeSearch);
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { render(s.toString()); }
            public void afterTextChanged(Editable s) {}
        });
        render("");
        return root;
    }

    private void render(String query) {
        if (results == null || getContext() == null) return;
        results.removeAllViews();
        String term = query.trim().toLowerCase(Locale.ROOT);
        int matches = 0;
        for (RecipeCatalog.Recipe recipe : RecipeCatalog.ALL) {
            if (!recipe.title.toLowerCase(Locale.ROOT).contains(term)
                    && !recipe.ingredients.toLowerCase(Locale.ROOT).contains(term)) continue;
            results.addView(RecipeCards.create(requireContext(), recipe, null));
            matches++;
        }
        if (matches == 0) {
            TextView empty = new TextView(requireContext());
            empty.setText(R.string.no_search_results);
            empty.setTextSize(18);
            results.addView(empty);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        results = null;
    }
}
