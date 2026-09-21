package com.example.cookpin.data;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PortionScalerTest {
    @Test
    public void fractionsScaleAndRemainReadable() {
        assertEquals("1 1/4 cups all-purpose flour",
                PortionScaler.ingredient("2 1/2 cups all-purpose flour", 0));
        assertEquals("1 cup milk", PortionScaler.ingredient("1/2 cup milk", 3));
        assertEquals("1 1/8 cups cocoa", PortionScaler.ingredient("3/4 cup cocoa", 2));
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
}
