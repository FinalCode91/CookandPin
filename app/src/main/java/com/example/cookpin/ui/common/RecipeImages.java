package com.example.cookpin.ui.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

/** Decodes recipe photos near display size and reuses them across screen refreshes. */
public final class RecipeImages {
    private static final int MAX_DIMENSION = 800;
    private static final int CACHE_BYTES = (int) Math.min(8L * 1024 * 1024,
            Runtime.getRuntime().maxMemory() / 8);
    private static final LruCache<Integer, Bitmap> CACHE = new LruCache<Integer, Bitmap>(CACHE_BYTES) {
        @Override
        protected int sizeOf(Integer key, Bitmap value) {
            return value.getAllocationByteCount();
        }
    };

    public static Bitmap get(Resources resources, int id) {
        Bitmap cached = CACHE.get(id);
        if (cached != null) return cached;

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, id, bounds);
        int longestSide = Math.max(bounds.outWidth, bounds.outHeight);
        int sample = 1;
        while (longestSide / (sample * 2L) >= MAX_DIMENSION) sample *= 2;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sample;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565; // Photos have no transparency.
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id, options);
        if (bitmap != null) CACHE.put(id, bitmap);
        return bitmap;
    }

    private RecipeImages() {}
}
