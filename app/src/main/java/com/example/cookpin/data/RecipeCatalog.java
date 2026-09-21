package com.example.cookpin.data;

import com.example.cookpin.R;
import java.util.Arrays;
import java.util.List;

public final class RecipeCatalog {
    public static final class Recipe {
        public final String id, title, ingredients, instructions;
        public final int image, minutes, servings;
        Recipe(String id, String title, String ingredients, String instructions, int image, int minutes, int servings) {
            this.id = id;
            this.title = title;
            this.ingredients = ingredients;
            this.instructions = instructions;
            this.image = image;
            this.minutes = minutes;
            this.servings = servings;
        }
    }

    public static final List<Recipe> ALL = Arrays.asList(
        new Recipe("recipe1", "Pizza", "2 1/2 cups all-purpose flour\n1 cup warm water\n2 1/4 tsp active dry yeast\n1 tbsp olive oil\n1 tsp salt\n1/2 cup pizza sauce\n1 1/2 cups shredded mozzarella", "1. Stir yeast into warm water and let stand 5 minutes.\n2. Mix flour and salt; add yeast water and olive oil. Knead until smooth and let rise in a covered bowl for about 1 hour.\n3. Heat oven to 425°F. Stretch dough onto an oiled baking pan; for larger batches use extra pans so the crust stays thin. Spread sauce and add cheese.\n4. Bake 12–18 minutes until the crust is golden and cheese is bubbling.", R.drawable.recipe1, 95, 4),
        new Recipe("recipe2", "Chocolate Cake", "1 3/4 cups all-purpose flour\n2 cups granulated sugar\n3/4 cup unsweetened cocoa powder\n1 1/2 tsp baking powder\n1 1/2 tsp baking soda\n1 tsp salt\n2 eggs\n1 cup milk\n1/2 cup vegetable oil\n2 tsp vanilla extract\n1 cup hot water", "1. Heat oven to 350°F. Grease one 9-inch round pan for each half batch (two pans for the original recipe); bake in batches if needed.\n2. Whisk flour, sugar, cocoa, baking powder, baking soda and salt.\n3. Beat in eggs, milk, oil and vanilla; then stir in hot water. The batter will be thin.\n4. Divide evenly between pans, keeping the batter at the same depth. Bake each pan about 30–35 minutes, until a toothpick inserted in the center comes out clean. Cool before serving.", R.drawable.recipe2, 50, 12),
        new Recipe("recipe3", "Potato Soup", "4 medium russet potatoes\n1 medium onion\n2 tbsp butter\n4 cups vegetable broth\n1 cup heavy cream\n1/2 tsp salt\n1/4 tsp black pepper", "1. Peel and dice potatoes; finely chop onion.\n2. Melt butter in a pot and soften onion for 5 minutes.\n3. Add potatoes and broth. Simmer 15–20 minutes until potatoes are tender.\n4. Mash some of the potatoes to thicken the soup, stir in cream, salt and pepper, and warm gently without boiling.", R.drawable.recipe3, 40, 4),
        new Recipe("recipe4", "Chicken Enchiladas", "2 cups cooked shredded chicken\n8 small tortillas\n2 cups enchilada sauce\n1 1/2 cups shredded cheese", "1. Heat oven to 350°F. Spread one quarter of the sauce in a baking dish.\n2. Mix cooked chicken with another quarter of the sauce. Divide among tortillas, roll, and place seam-side down in the dish.\n3. Pour remaining sauce over tortillas, sprinkle with cheese, and bake 20–25 minutes until the center reaches 165°F.", R.drawable.recipe4, 40, 4),
        new Recipe("recipe5", "Chocolate Chip Cookies", "1 cup softened butter\n3/4 cup granulated sugar\n3/4 cup packed brown sugar\n2 eggs\n2 1/4 cups all-purpose flour\n1 tsp baking soda\n1 tsp salt\n2 cups chocolate chips", "1. Heat oven to 350°F and line baking sheets. Use more sheets or bake in batches when scaling up.\n2. Cream butter with both sugars. Beat in eggs.\n3. Mix in flour, baking soda and salt; fold in chocolate chips.\n4. Drop tablespoon-sized portions onto sheets with space between them. Bake 10–12 minutes until edges are golden. Cool on the pan for 5 minutes.", R.drawable.recipe5, 30, 24),
        new Recipe("recipe6", "Stir Fry", "1 tbsp olive oil\n1 red bell pepper\n1 yellow bell pepper\n1 cup sugar snap peas\n2 carrots\n1 cup sliced mushrooms\n2 cups broccoli florets\n1 cup baby corn\n1/2 cup sliced water chestnuts\n3 tbsp soy sauce\n2 garlic cloves\n1 tbsp brown sugar\n1 tsp sesame oil\n1/2 cup vegetable broth\n1 tbsp cornstarch", "1. Slice peppers and carrots; mince garlic. Whisk soy sauce, brown sugar, sesame oil, broth and cornstarch in a bowl.\n2. Heat olive oil in a large skillet or wok. Cook carrots and broccoli for 3 minutes.\n3. Add peppers, peas, mushrooms, baby corn, water chestnuts and garlic; cook 3–5 minutes until crisp-tender.\n4. Pour in sauce and stir until thickened and bubbling. Serve hot.", R.drawable.recipe6, 25, 4),
        new Recipe("recipe7", "Cauliflower Casserole", "1 head cauliflower\n2 tbsp butter\n1/2 cup grated Parmesan cheese\n1/2 cup bread crumbs\n1/2 tsp salt\n1/4 tsp red pepper flakes", "1. Heat oven to 350°F. Cut cauliflower into florets and steam about 10 minutes until just tender. Drain well.\n2. Combine melted butter, Parmesan, bread crumbs, salt and pepper flakes.\n3. Place cauliflower in a greased baking dish and scatter crumb mixture on top.\n4. Bake about 20 minutes until golden and hot.", R.drawable.recipe7, 40, 4),
        new Recipe("recipe8", "Pumpkin Pie", "1 unbaked 9-inch deep-dish pie shell\n1 can (15 oz each) pumpkin puree\n1 can (14 oz each) sweetened condensed milk\n2 large eggs\n1 tbsp pumpkin pie spice", "1. Heat oven to 425°F. Whisk pumpkin, condensed milk, eggs and spice until smooth.\n2. Pour filling into the unbaked pie shell. Bake at 425°F for 15 minutes.\n3. Lower oven to 350°F and bake another 40–50 minutes, until a knife inserted near the center comes out clean.\n4. Cool on a wire rack for about 2 hours before slicing. Refrigerate leftovers.", R.drawable.recipe8, 195, 8),
        new Recipe("recipe9", "Meatballs", "1 lb ground beef\n1/2 cup bread crumbs\n1/4 cup milk\n1/4 cup finely chopped onion\n2 tbsp chopped parsley\n1/4 cup grated Parmesan cheese\n1 egg\n1/2 tsp salt\n1/4 tsp black pepper", "1. Heat oven to 400°F and line a baking sheet.\n2. Mix beef, bread crumbs, milk, onion, parsley, Parmesan, egg, salt and pepper just until combined.\n3. Form small meatballs and arrange them in a single layer.\n4. Bake 18–22 minutes, checking that the centers reach 160°F with a food thermometer.", R.drawable.recipe9, 35, 4)
    );

    public static Recipe find(String id) {
        for (Recipe recipe : ALL) {
            if (recipe.id.equals(id)) return recipe;
        }
        return null;
    }

    private RecipeCatalog() {}
}
