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
        Matcher match = AMOUNT.matcher(line);
        if (!match.matches()) return line;
        double value = parse(match.group(1)) * FACTORS[clamp(scale)];
        String rest = match.group(2);
        String[][] nouns = {{"cup", "cups"}, {"can", "cans"}, {"egg", "eggs"},
                {"head", "heads"}, {"carrot", "carrots"}, {"clove", "cloves"},
                {"onion", "onions"}, {"pepper", "peppers"}, {"shell", "shells"},
                {"tortilla", "tortillas"}};
        for (String[] noun : nouns) {
            rest = rest.replaceAll("\\b" + noun[0] + "s?\\b",
                    value > 1 ? noun[1] : noun[0]);
        }
        return format(value) + " " + rest;
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
        int eighths = (int) Math.round(amount * 8);
        int whole = eighths / 8;
        int remainder = eighths % 8;
        if (remainder == 0) return Integer.toString(whole);
        int divisor = remainder % 4 == 0 ? 4 : remainder % 2 == 0 ? 2 : 1;
        String fraction = (remainder / divisor) + "/" + (8 / divisor);
        return whole == 0 ? fraction : whole + " " + fraction;
    }

    private PortionScaler() {}
}
