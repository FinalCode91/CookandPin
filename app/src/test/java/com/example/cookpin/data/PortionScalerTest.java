package com.example.cookpin.data;

import com.example.cookpin.R;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class PortionScalerTest {
    private static final Pattern QUANTITY = Pattern.compile("^(\\d+(?: \\d+/\\d+|/\\d+)?) ");

    @Test
    public void twentyAddedRecipesHaveUniqueIdsSourcesAndNumberedSteps() {
        assertEquals(29, RecipeCatalog.ALL.size());
        Set<String> ids = new HashSet<>();
        for (RecipeCatalog.Recipe recipe : RecipeCatalog.ALL) {
            assertTrue("Duplicate recipe ID: " + recipe.id, ids.add(recipe.id));
            assertTrue(recipe.minutes > 0);
            if (Integer.parseInt(recipe.id.substring("recipe".length())) < 10) continue;
            assertTrue(recipe.id, recipe.sourceUrl.startsWith("https://"));
            assertEquals(recipe.id, R.drawable.ic_recipe_placeholder, recipe.image);
            String[] steps = recipe.instructions.split("\\n");
            for (int i = 0; i < steps.length; i++)
                assertTrue(recipe.id + " step " + (i + 1), steps[i].startsWith((i + 1) + ". "));
        }
    }

    @Test
    public void fractionsScaleAndRemainReadable() {
        assertEquals("1 1/4 cups all-purpose flour",
                PortionScaler.ingredient("2 1/2 cups all-purpose flour", 0));
        assertEquals("1 cup milk", PortionScaler.ingredient("1/2 cup milk", 3));
        assertEquals("1 1/8 cups cocoa", PortionScaler.ingredient("3/4 cup cocoa", 2));
        assertEquals("1/6 cup oil", PortionScaler.ingredient("1/3 cup oil", 0));
        assertEquals("2/3 cup oil", PortionScaler.ingredient("1/3 cup oil", 3));
        assertEquals("1/16 tsp spice", PortionScaler.ingredient("1/8 tsp spice", 0));
        assertEquals("1 tomato", PortionScaler.ingredient("2 tomatoes", 0));
        assertEquals("2 potatoes", PortionScaler.ingredient("1 potato", 3));
    }

    @Test
    public void countableIngredientsAndServingsScale() {
        assertEquals("1 egg", PortionScaler.ingredient("2 eggs", 0));
        assertEquals("2 cans (15 oz each) pumpkin puree",
                PortionScaler.ingredient("1 can (15 oz each) pumpkin puree", 3));
        assertEquals(4, PortionScaler.servings(RecipeCatalog.find("recipe8"), 0));
        assertEquals(16, PortionScaler.servings(RecipeCatalog.find("recipe8"), 3));
        assertEquals("1 unbaked 6-inch pie shell", PortionScaler.ingredient(
                "1 unbaked 9-inch deep-dish pie shell", 0));
        assertEquals("2 unbaked 9-inch deep-dish pie shells", PortionScaler.ingredient(
                "1 unbaked 9-inch deep-dish pie shell", 3));
        org.junit.Assert.assertTrue(PortionScaler.instructions(
                RecipeCatalog.find("recipe8"), 0).contains("6-inch"));
    }

    @Test
    public void everyCatalogIngredientScalesWithoutDroppingItemsOrCollidingShoppingKeys() {
        double[] factors = {0.5, 1.0, 1.5, 2.0};
        Set<String> shoppingKeys = new HashSet<>();
        for (RecipeCatalog.Recipe recipe : RecipeCatalog.ALL) {
            assertTrue(recipe.servings > 0);
            String[] originals = recipe.ingredients.split("\\n");
            assertEquals("Ingredient count changed for " + recipe.id, originals.length,
                    shoppingKeysForRecipe(recipe, shoppingKeys));
            for (int scale = 0; scale < factors.length; scale++) {
                String[] adjusted = PortionScaler.ingredients(recipe, scale).split("\\n");
                assertEquals(recipe.id, originals.length, adjusted.length);
                assertEquals(recipe.id, Math.round(recipe.servings * factors[scale]),
                        PortionScaler.servings(recipe, scale));
                for (int i = 0; i < originals.length; i++) {
                    if (originals[i].contains("9-inch deep-dish pie shell")) continue;
                    assertEquals(recipe.id + " ingredient " + i,
                            quantity(originals[i]) * factors[scale], quantity(adjusted[i]), 0.0001);
                }
            }
        }
    }

    private static int shoppingKeysForRecipe(RecipeCatalog.Recipe recipe, Set<String> keys) {
        int before = keys.size();
        for (String ingredient : recipe.ingredients.split("\\n")) {
            assertTrue("Duplicate shopping key: " + recipe.id + ":" + ingredient,
                    keys.add(recipe.id + ":" + ingredient));
        }
        return keys.size() - before;
    }

    private static double quantity(String line) {
        Matcher matcher = QUANTITY.matcher(line);
        assertTrue("Missing leading quantity: " + line, matcher.find());
        double sum = 0;
        for (String term : matcher.group(1).split(" ")) {
            String[] fraction = term.split("/");
            sum += fraction.length == 1 ? Double.parseDouble(term)
                    : Double.parseDouble(fraction[0]) / Double.parseDouble(fraction[1]);
        }
        return sum;
    }
}
