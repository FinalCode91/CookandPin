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
import com.example.cookpin.data.PortionPreferences;
import com.example.cookpin.data.PortionScaler;
import com.example.cookpin.data.RecipeCatalog;
import com.example.cookpin.data.ShoppingRecipes;
import com.example.cookpin.ui.common.RecipeImages;

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
        TextView ingredientList = findViewById(R.id.recipeIngredientsList);
        ingredientList.setText(ingredients == null ? "" : ingredients);
        TextView instructionList = findViewById(R.id.recipeInstructionsList);
        instructionList.setText(instructions == null ? "" : instructions);

        ImageView photo = findViewById(R.id.recipePhoto);
        Button pin = findViewById(R.id.pinRecipe);
        Button shopping = findViewById(R.id.shoppingRecipe);
        View portionControls = findViewById(R.id.portionControls);
        if (recipe != null) {
            photo.setImageBitmap(RecipeImages.get(getResources(), recipe.image));
            photo.setContentDescription(recipe.title);
            updatePinButton(pin, recipe.id);
            pin.setOnClickListener(v -> {
                PinnedRecipes.setPinned(this, recipe.id, !PinnedRecipes.contains(this, recipe.id));
                updatePinButton(pin, recipe.id);
            });
            updateShoppingButton(shopping, recipe.id);
            shopping.setOnClickListener(v -> {
                ShoppingRecipes.setSelected(this, recipe.id, !ShoppingRecipes.contains(this, recipe.id));
                updateShoppingButton(shopping, recipe.id);
            });
            Button decrease = findViewById(R.id.decreasePortions);
            Button increase = findViewById(R.id.increasePortions);
            Runnable refresh = () -> {
                int scale = PortionPreferences.get(this, recipe.id);
                String[] labels = {getString(R.string.half_batch), getString(R.string.original_batch),
                        getString(R.string.one_and_half_batch), getString(R.string.double_batch)};
                ((TextView) findViewById(R.id.portionSummary)).setText(getString(
                        R.string.portion_summary, PortionScaler.servings(recipe, scale), labels[scale]));
                ingredientList.setText(PortionScaler.ingredients(recipe, scale));
                instructionList.setText(PortionScaler.instructions(recipe, scale));
                decrease.setEnabled(scale > 0);
                increase.setEnabled(scale < 3);
            };
            decrease.setOnClickListener(v -> {
                PortionPreferences.set(this, recipe, PortionPreferences.get(this, recipe.id) - 1);
                if (ShoppingRecipes.contains(this, recipe.id))
                    android.widget.Toast.makeText(this, R.string.shopping_reset, android.widget.Toast.LENGTH_SHORT).show();
                refresh.run();
            });
            increase.setOnClickListener(v -> {
                PortionPreferences.set(this, recipe, PortionPreferences.get(this, recipe.id) + 1);
                if (ShoppingRecipes.contains(this, recipe.id))
                    android.widget.Toast.makeText(this, R.string.shopping_reset, android.widget.Toast.LENGTH_SHORT).show();
                refresh.run();
            });
            refresh.run();
        } else {
            photo.setVisibility(View.GONE);
            pin.setVisibility(View.GONE);
            shopping.setVisibility(View.GONE);
            portionControls.setVisibility(View.GONE);
            findViewById(R.id.portionNote).setVisibility(View.GONE);
        }
    }

    private void updatePinButton(Button button, String id) {
        button.setText(PinnedRecipes.contains(this, id) ? R.string.unpin_recipe : R.string.pin_recipe);
    }

    private void updateShoppingButton(Button button, String id) {
        button.setText(ShoppingRecipes.contains(this, id)
                ? R.string.remove_from_shopping : R.string.add_to_shopping);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
