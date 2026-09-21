package com.example.cookpin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.cookpin.data.PinnedRecipes;
import com.example.cookpin.data.RecipeCatalog;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecipeCatalog.Recipe recipe = RecipeCatalog.find(getIntent().getStringExtra("recipeId"));
        String title = recipe == null ? getIntent().getStringExtra("recipeTitle") : recipe.title;
        String ingredients = recipe == null ? getIntent().getStringExtra("recipeIngredients") : recipe.ingredients;
        String instructions = recipe == null ? getIntent().getStringExtra("recipeInstructions") : recipe.instructions;

        ((TextView) findViewById(R.id.recipeTitleText)).setText(
                title == null ? getString(R.string.recipe_unavailable) : title);
        TextView time = findViewById(R.id.recipeTime);
        if (recipe == null) time.setVisibility(View.GONE);
        else time.setText(getString(R.string.estimated_time, recipe.minutes));
        ((TextView) findViewById(R.id.recipeIngredientsList)).setText(
                ingredients == null ? "" : ingredients);
        ((TextView) findViewById(R.id.recipeInstructionsList)).setText(
                instructions == null ? "" : instructions);

        ImageView photo = findViewById(R.id.recipePhoto);
        Button pin = findViewById(R.id.pinRecipe);
        if (recipe != null) {
            photo.setImageResource(recipe.image);
            photo.setContentDescription(recipe.title);
            updatePinButton(pin, recipe.id);
            pin.setOnClickListener(v -> {
                PinnedRecipes.setPinned(this, recipe.id, !PinnedRecipes.contains(this, recipe.id));
                updatePinButton(pin, recipe.id);
            });
        } else {
            photo.setVisibility(View.GONE);
            pin.setVisibility(View.GONE);
        }
    }

    private void updatePinButton(Button button, String id) {
        button.setText(PinnedRecipes.contains(this, id) ? R.string.unpin_recipe : R.string.pin_recipe);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
