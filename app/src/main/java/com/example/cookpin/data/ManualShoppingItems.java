package com.example.cookpin.data;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Items added directly to Shopping; stored alongside, but independently of, recipe checkmarks. */
public final class ManualShoppingItems {
    private static final String PREFS = "cookandpin_shopping";
    private static final String KEY = "manual_items_v1";

    public static final class Item {
        public final String id;
        public final String name;
        public final boolean checked;

        private Item(String id, String name, boolean checked) {
            this.id = id;
            this.name = name;
            this.checked = checked;
        }
    }

    public static List<Item> get(Context context) {
        String value = preferences(context).getString(KEY, "[]");
        try {
            JSONArray array = new JSONArray(value);
            List<Item> result = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject entry = array.optJSONObject(i);
                if (entry == null) continue;
                String id = entry.optString("id", "");
                String name = entry.optString("name", "").trim();
                if (!id.isEmpty() && !name.isEmpty())
                    result.add(new Item(id, name, entry.optBoolean("checked", false)));
            }
            return result;
        } catch (JSONException exception) {
            return Collections.emptyList();
        }
    }

    public static void add(Context context, String name) {
        String trimmed = name.trim();
        if (trimmed.isEmpty()) return;
        List<Item> items = get(context);
        items.add(new Item(UUID.randomUUID().toString(), trimmed, false));
        save(context, items);
    }

    public static void setChecked(Context context, String id, boolean checked) {
        List<Item> items = get(context);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.id.equals(id)) {
                items.set(i, new Item(item.id, item.name, checked));
                save(context, items);
                return;
            }
        }
    }

    public static void remove(Context context, String id) {
        List<Item> items = get(context);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id.equals(id)) {
                items.remove(i);
                save(context, items);
                return;
            }
        }
    }

    private static void save(Context context, List<Item> items) {
        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject entry = new JSONObject();
            try {
                entry.put("id", item.id);
                entry.put("name", item.name);
                entry.put("checked", item.checked);
                array.put(entry);
            } catch (JSONException exception) {
                throw new IllegalStateException("Could not save shopping item", exception);
            }
        }
        preferences(context).edit().putString(KEY, array.toString()).apply();
    }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private ManualShoppingItems() {}
}
