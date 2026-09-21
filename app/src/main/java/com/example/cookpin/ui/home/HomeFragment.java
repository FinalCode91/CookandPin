package com.example.cookpin.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cookpin.R;
import com.example.cookpin.data.RecipeCatalog;
import com.example.cookpin.ui.common.RecipeCards;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private LinearLayout results;
    private ScrollView scroll;
    private EditText search;
    private CheckBox quickRecipes;
    private static final String SEARCH_STATE = "search";
    private static final String QUICK_STATE = "quick";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        results = root.findViewById(R.id.recipeResults);
        scroll = root.findViewById(R.id.discoverScroll);
        search = root.findViewById(R.id.recipeSearch);
        quickRecipes = root.findViewById(R.id.quickRecipes);
        if (savedInstanceState != null) {
            search.setText(savedInstanceState.getString(SEARCH_STATE, ""));
            quickRecipes.setChecked(savedInstanceState.getBoolean(QUICK_STATE));
        }
        quickRecipes.setOnCheckedChangeListener((button, checked) -> {
            render(search.getText().toString());
            resetResultsScroll();
        });
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                render(s.toString());
                resetResultsScroll();
            }
            public void afterTextChanged(Editable s) {}
        });
        render(search.getText().toString());
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (search != null) outState.putString(SEARCH_STATE, search.getText().toString());
        if (quickRecipes != null) outState.putBoolean(QUICK_STATE, quickRecipes.isChecked());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (search != null) render(search.getText().toString());
    }

    private void render(String query) {
        if (results == null || getContext() == null) return;
        results.removeAllViews();
        String term = query.trim().toLowerCase(Locale.ROOT);
        int matches = 0;
        for (RecipeCatalog.Recipe recipe : RecipeCatalog.ALL) {
            if (quickRecipes != null && quickRecipes.isChecked() && recipe.minutes > 30) continue;
            if (!recipe.title.toLowerCase(Locale.ROOT).contains(term)
                    && !recipe.ingredients.toLowerCase(Locale.ROOT).contains(term)) continue;
            results.addView(RecipeCards.create(requireContext(), recipe, null));
            matches++;
        }
        if (matches == 0) {
            TextView empty = new TextView(requireContext());
            empty.setText(quickRecipes != null && quickRecipes.isChecked()
                    ? R.string.no_filtered_results : R.string.no_search_results);
            empty.setTextSize(18);
            results.addView(empty);
            if (!term.isEmpty()) {
                Button clear = new Button(requireContext());
                clear.setText(R.string.clear_search);
                clear.setOnClickListener(view -> search.setText(""));
                results.addView(clear);
            }
        }
    }

    private void resetResultsScroll() {
        if (scroll != null) {
            scroll.scrollTo(0, 0);
            scroll.post(() -> {
                if (scroll != null) scroll.scrollTo(0, 0);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        results = null;
        scroll = null;
        search = null;
        quickRecipes = null;
    }
}
