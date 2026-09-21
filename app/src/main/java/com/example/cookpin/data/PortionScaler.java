package com.example.cookpin.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Estimates ingredient amounts for half, original, one-and-a-half, or double batches. */
public final class PortionScaler {
    private static final Pattern AMOUNT = Pattern.compile("^(\\d+\\s+\\d+/\\d+|\\d+/\\d+|\\d+)\\s+(.+)$");
    private static final double[] FACTORS = {0.5, 1.0, 1.5, 2.0};
    public static final int ORIGINAL = 1;

    public static int clamp(int scale) {
        return Math.max(0, Math.min(FACTORS.length - 1, scale));
    }

    public static int servings(RecipeCatalog.Recipe recipe, int scale) {
        return (int) Math.round(recipe.servings * FACTORS[clamp(scale)]);
    }

    public static String ingredients(RecipeCatalog.Recipe recipe, int scale) {
        String[] lines = recipe.ingredients.split("\\n");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (result.length() > 0) result.append('\n');
            result.append(ingredient(line, scale));
        }
        return result.toString();
    }

    public static String ingredient(String line, int scale) {
        if (clamp(scale) == ORIGINAL) return line;
        if (line.equals("1 unbaked 9-inch deep-dish pie shell")) {
            switch (clamp(scale)) {
                case 0: return "1 unbaked 6-inch pie shell";
                case 2: return "1 unbaked 9-inch deep-dish pie shell and 1 unbaked 6-inch pie shell";
                case 3: return "2 unbaked 9-inch deep-dish pie shells";
                default: return line;
            }
        }
        Matcher match = AMOUNT.matcher(line);
        if (!match.matches()) return line;
        double value = parse(match.group(1)) * FACTORS[clamp(scale)];
        String rest = match.group(2);
        String[][] nouns = {{"cup", "cups"}, {"can", "cans"}, {"egg", "eggs"},
                {"head", "heads"}, {"carrot", "carrots"}, {"clove", "cloves"},
                {"onion", "onions"}, {"pepper", "peppers"}, {"shell", "shells"},
                {"tortilla", "tortillas"}, {"apple", "apples"}, {"banana", "bananas"},
                {"tomato", "tomatoes"}, {"potato", "potatoes"}, {"lemon", "lemons"},
                {"lime", "limes"}, {"rib", "ribs"}, {"sprig", "sprigs"},
                {"slice", "slices"}, {"breast", "breasts"}};
        for (String[] noun : nouns) {
            rest = rest.replaceAll("\\b(?:" + noun[0] + "|" + noun[1] + ")\\b",
                    value > 1 ? noun[1] : noun[0]);
        }
        return format(value) + " " + rest;
    }

    public static String instructions(RecipeCatalog.Recipe recipe, int scale) {
        if (!"recipe8".equals(recipe.id) || clamp(scale) == ORIGINAL) return recipe.instructions;
        String start = "1. Heat oven to 425°F. Whisk pumpkin, condensed milk, eggs and spice until smooth.\n";
        String finish = "\n4. Cool pies on a wire rack for about 2 hours before slicing. Refrigerate leftovers.";
        if (clamp(scale) == 0) {
            return start + "2. Pour filling into a 6-inch pie shell. Bake at 425°F for 15 minutes.\n"
                    + "3. Lower oven to 350°F. Start checking after 20 minutes; keep baking until a knife inserted near the center comes out clean."
                    + finish;
        }
        if (clamp(scale) == 2) {
            return start + "2. Divide filling between a 9-inch deep-dish shell and a 6-inch shell, keeping the filling at a similar depth. Bake at 425°F for 15 minutes.\n"
                    + "3. Lower oven to 350°F. Start checking the smaller pie after 20 minutes and the larger after 40 minutes; remove each when a knife inserted near the center comes out clean."
                    + finish;
        }
        return start + "2. Divide filling between two 9-inch deep-dish shells. Bake at 425°F for 15 minutes.\n"
                + "3. Lower oven to 350°F. Bake another 40–50 minutes, checking each pie until a knife inserted near the center comes out clean."
                + finish;
    }

    private static double parse(String amount) {
        String[] pieces = amount.split(" ");
        double total = 0;
        for (String piece : pieces) {
            if (piece.contains("/")) {
                String[] fraction = piece.split("/");
                total += Double.parseDouble(fraction[0]) / Double.parseDouble(fraction[1]);
            } else total += Double.parseDouble(piece);
        }
        return total;
    }

    private static String format(double amount) {
        // Forty-eighths preserve the thirds, sixths and sixteenths used in recipes.
        // Rounding to eighths would turn half of 1/3 cup into 1/8 cup.
        int fortyEighths = (int) Math.round(amount * 48);
        int whole = fortyEighths / 48;
        int remainder = fortyEighths % 48;
        if (remainder == 0) return Integer.toString(whole);
        int divisor = gcd(remainder, 48);
        String fraction = (remainder / divisor) + "/" + (48 / divisor);
        return whole == 0 ? fraction : whole + " " + fraction;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int next = a % b;
            a = b;
            b = next;
        }
        return a;
    }

    private PortionScaler() {}
}
