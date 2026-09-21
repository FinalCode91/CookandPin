package com.example.cookpin;

import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
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
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String STATE_COOKING = "cooking_mode";
    private static final String STATE_STEP = "cooking_step";
    private View recipeDetails;
    private View cookingMode;
    private String[] cookingSteps;
    private int cookingStep;
    private boolean isCooking;
    private OnBackPressedCallback cookingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setSupportActionBar(findViewById(R.id.recipeToolbar));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.recipe_toolbar_title);
        }

        RecipeCatalog.Recipe recipe = RecipeCatalog.find(getIntent().getStringExtra("recipeId"));
        String title = recipe == null ? getIntent().getStringExtra("recipeTitle") : recipe.title;
        String ingredients = recipe == null ? getIntent().getStringExtra("recipeIngredients") : recipe.ingredients;
        String instructions = recipe == null ? getIntent().getStringExtra("recipeInstructions") : recipe.instructions;
        recipeDetails = findViewById(R.id.recipeDetails);
        cookingMode = findViewById(R.id.cookingMode);
        cookingBack = new OnBackPressedCallback(false) {
            @Override public void handleOnBackPressed() { showCooking(false); }
        };
        getOnBackPressedDispatcher().addCallback(this, cookingBack);

        ((TextView) findViewById(R.id.recipeTitleText)).setText(
                title == null ? getString(R.string.recipe_unavailable) : title);
        ((TextView) findViewById(R.id.cookingRecipeTitle)).setText(
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
        Button startCooking = findViewById(R.id.startCooking);
        Button source = findViewById(R.id.recipeSource);
        View portionControls = findViewById(R.id.portionControls);
        if (recipe != null) {
            if (recipe.image == R.drawable.ic_recipe_placeholder) {
                photo.setImageResource(recipe.image);
                photo.setContentDescription(getString(R.string.recipe_illustration));
            } else {
                photo.setImageBitmap(RecipeImages.get(getResources(), recipe.image));
                photo.setContentDescription(recipe.title);
            }
            if (recipe.sourceUrl != null) {
                source.setVisibility(View.VISIBLE);
                source.setOnClickListener(v -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.sourceUrl)));
                    } catch (ActivityNotFoundException exception) {
                        android.widget.Toast.makeText(this, R.string.no_browser_available,
                                android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
            startCooking.setOnClickListener(v -> {
                cookingSteps = PortionScaler.instructions(recipe, PortionPreferences.get(this, recipe.id))
                        .split("\\n(?=\\d+\\.\\s)");
                cookingStep = 0;
                showCooking(true);
            });
        } else {
            photo.setVisibility(View.GONE);
            pin.setVisibility(View.GONE);
            shopping.setVisibility(View.GONE);
            startCooking.setVisibility(View.GONE);
            portionControls.setVisibility(View.GONE);
            findViewById(R.id.portionNote).setVisibility(View.GONE);
        }
        findViewById(R.id.closeCooking).setOnClickListener(v -> showCooking(false));
        findViewById(R.id.previousCookingStep).setOnClickListener(v -> {
            cookingStep--;
            renderCookingStep();
        });
        findViewById(R.id.nextCookingStep).setOnClickListener(v -> {
            cookingStep++;
            renderCookingStep();
        });
        if (recipe != null && savedInstanceState != null && savedInstanceState.getBoolean(STATE_COOKING)) {
            cookingSteps = PortionScaler.instructions(recipe, PortionPreferences.get(this, recipe.id))
                    .split("\\n(?=\\d+\\.\\s)");
            cookingStep = Math.max(0, Math.min(savedInstanceState.getInt(STATE_STEP), cookingSteps.length - 1));
            showCooking(true);
        }
    }

    private void showCooking(boolean visible) {
        isCooking = visible;
        recipeDetails.setVisibility(visible ? View.GONE : View.VISIBLE);
        cookingMode.setVisibility(visible ? View.VISIBLE : View.GONE);
        cookingMode.setKeepScreenOn(visible);
        cookingBack.setEnabled(visible);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(visible
                    ? R.string.cooking_toolbar_title : R.string.recipe_toolbar_title);
        }
        if (visible) {
            renderCookingStep();
            findViewById(R.id.cookingStepCounter).requestFocus();
        }
    }

    private void renderCookingStep() {
        if (cookingSteps == null || cookingSteps.length == 0) return;
        ((TextView) findViewById(R.id.cookingStepCounter)).setText(getString(
                R.string.cooking_step_count, cookingStep + 1, cookingSteps.length));
        ((TextView) findViewById(R.id.cookingStepText)).setText(
                cookingSteps[cookingStep].trim().replaceFirst("^\\d+\\.\\s*", ""));
        LinearProgressIndicator progress = findViewById(R.id.cookingProgress);
        progress.setMax(cookingSteps.length);
        progress.setProgressCompat(cookingStep + 1, true);
        findViewById(R.id.previousCookingStep).setEnabled(cookingStep > 0);
        findViewById(R.id.nextCookingStep).setEnabled(cookingStep < cookingSteps.length - 1);
        findViewById(R.id.cookingStepCounter).announceForAccessibility(getString(
                R.string.cooking_step_count, cookingStep + 1, cookingSteps.length));
    }

    @Override
    protected void onSaveInstanceState(@androidx.annotation.NonNull Bundle outState) {
        outState.putBoolean(STATE_COOKING, isCooking);
        outState.putInt(STATE_STEP, cookingStep);
        super.onSaveInstanceState(outState);
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
        if (isCooking) {
            showCooking(false);
            return true;
        }
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
