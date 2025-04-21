package com.example.cookpin.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cookpin.RecipeDetailActivity;
import com.example.cookpin.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        setupClick(binding.Pizza, "Pizza",
                "Flour, Water, Yeast, Tomato Sauce, Cheese",
                "Mix flour, water, and yeast.. roll the dough, bake with toppings at 375 degrees for 20-25 minutes.");
        setupClick(binding.ChocolateCake, "Chocolate Cake",
                "Sugar, Cocoa Powder, Baking Soda, Baking Powder, Salt, Flour, Buttermilk, Hot Water, Oil, Eggs, Vanilla Extract",
                "Whisk dry ingredients into a large bowl, stir together wet ingredients in another bowl, and now add them together and whisk until batter forms without lumps. Pour into a cake pan at 350 degrees for 30-35 minutes.");
        setupClick(binding.PotatoSoup, "Potato Soup",
                "Diced Onions, Butter, Russet Potatoes, Heavy Cream",
                "Saute onions: In a large pot, add the butter over medium heat, then add in onions until they're tender. Boil the potatoes: add the potatoes into the pot with water, bring to a boil and reduce to simmer for 15-20 minutes until the potatoes are tender. Drain the water. Add the onions, cream and remaining butter to the pot. Gently mash the potatoes, cook for 5 minutes and it's ready to serve.");
        setupClick(binding.ChickenEnchiladas, "Chicken Enchiladas",
                "Tortillas, Enchilada Sauce, Shredded Chicken, Shredded Cheese",
                "Preheat oven to 350 degrees. In a large bowl, combine shredded chicken with 1/4 cup of enchilada sauce. Warm the tortillas in the microwave for 1 minute, flipping them half way through. Assemble the enchiladas, roll them tightly, and place them into a large baking dish. Pour the remaining sauce over them and top with cheese. Bake for 20 minutes.");
        setupClick(binding.ChocolateChipCookies, "Chocolate Chip Cookies",
                "Butter, Brown Sugar, Sugar, Flour, Baking Soda, Salt, Chocolate Chips",
                "Preheat the oven to 350 degrees. Beat the butter with brown and white sugar until creamy and pale in color. Add the flour, baking soda, and salt, mixing until combined. Fold in chocolate chips. Scoop out 15-20 balls and arrange them on a lined baking sheet with parchment paper. Bake for about 10 minutes. ");
        setupClick(binding.StirFry, "Stir Fry",
                "Olive Oil, Red Bell Pepper, Yellow Bell Pepper, Sugar Snap Peas, Carrots, Mushrooms, Broccoli, Baby Corn, Water Chestnuts, Soy Sauce, Garlic Cloves, Brown Sugar, Sesame Oil, Chicken Broth, Cornstarch",
                "In a wok, add 1 tbsp of olive oil over medium-high heat. Add in your vegetables and saute for about 2-3 minutes until veggies are tender. In a small bowl, whisk together your sauces, brown sugar and cornstarch. Pour over the veggies and cook until the sauce has thickened.");
        setupClick(binding.CheesyCauliflowerCasserole, "Cauliflower Casserole",
                "Water, Cauliflower, Butter, Parmesan Cheese, Bread Crumbs, Salt, Red Pepper Flakes",
                "Preheat oven to 350 degrees. Bring 2 inches of water to a boil in a saucepan. Add cauliflower, cover, and cook for about 10 minutes. In a small bowl, mix together butter, parmesean, bread crumbs, salt and red pepper flakes. Mix everything together and place into a baking dish. Bake for 20 minutes.");
        setupClick(binding.PumpkinPie, "Pumpkin Pie",
                "1 Can of Pumpkin Puree, 1 Can of Sweetened Condensed Milk, 2 Large Eggs, 1 TBSP Pumpkin Pie Spice",
                "Preheat the oven to 425 degrees. Mix the pumpkin puree, sweetened condensed milk, eggs and pumpkin pie spice in a large bowl and whisk until combined. Pour the filling into a 9 inch deep pie shell. Bake for 15 minutes");
        setupClick(binding.Meatballs, "Meatballs",
                "Beef, Breadcrumbs, Milk, Onion, Parsley, Parmesan, Cheese, Egg, Any Other Seasonings",
                "Preheat oven to 400 degrees. In a medium bowl, add beef, breadcrumbs,milk, onion, parsley, parmesan, cheese, egg, and seasonings. Mix them all together. Then, shape the meat mixture into about 48 meatballs and place them on a prepared baking sheet. Bake for 18-20 minutes. Serve with tomato sauce if desired.");

        return root;
    }

    private void setupClick(View imageView, String recipeTitle, String recipeIngredients, String recipeInstructions)
    {
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("recipeTitle", recipeTitle);
            intent.putExtra("recipeIngredients", recipeIngredients);
            intent.putExtra("recipeInstructions", recipeInstructions);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}