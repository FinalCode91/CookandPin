package com.example.cookpin;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Recipe Details");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String recipeTitle = getIntent().getStringExtra("recipeTitle");
        String recipeIngredients = getIntent().getStringExtra("recipeIngredients");
        String recipeInstructions = getIntent().getStringExtra("recipeInstructions");

        TextView titleTextView = findViewById(R.id.recipeTitleText);
        TextView ingredientsTextView = findViewById(R.id.recipeIngredients);
        TextView instructionsTextView = findViewById(R.id.recipeInstructions);

        titleTextView.setText(recipeTitle);
        ingredientsTextView.setText(recipeIngredients);
        instructionsTextView.setText(recipeInstructions);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}