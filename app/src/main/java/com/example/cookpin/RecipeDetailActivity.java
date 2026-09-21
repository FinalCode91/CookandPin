package com.example.cookpin;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Recipe Details");
        }

        String recipeTitle = getIntent().getStringExtra("recipeTitle");
        String recipeIngredients = getIntent().getStringExtra("recipeIngredients");
        String recipeInstructions = getIntent().getStringExtra("recipeInstructions");

        TextView titleTextView = findViewById(R.id.recipeTitleText);
        TextView ingredientsListTextView = findViewById(R.id.recipeIngredientsList);
        TextView instructionsListTextView = findViewById(R.id.recipeInstructionsList);

        titleTextView.setText(recipeTitle != null ? recipeTitle : getString(R.string.recipe_unavailable));
        ingredientsListTextView.setText(recipeIngredients != null ? recipeIngredients : "");
        instructionsListTextView.setText(recipeInstructions != null ? recipeInstructions : "");
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
