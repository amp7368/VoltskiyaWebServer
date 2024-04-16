package apple.voltskiya.webroot.model.json.util;

import apple.voltskiya.webroot.model.json.DJsonDocument;
import apple.voltskiya.webroot.model.json.DJsonField;
import apple.voltskiya.webroot.model.json.JsonFindApi;
import apple.voltskiya.webroot.model.json.JsonUpdateApi;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public class UpdateJsonService {

    private static final String SEPARATOR = ".";

    public static Set<DJsonField> update(DJsonDocument document, JsonObject next) {
        Map<String, DJsonField> flattenedPrev = JsonFindApi.queryCurrentFields(document.getId());
        Map<String, String> flattenedNext = flatten(next);
        return update(document, flattenedPrev, flattenedNext);
    }

    public static Set<DJsonField> update(DJsonDocument document, Map<String, DJsonField> flattenedPrev,
        Map<String, String> flattenedNext) {
        Set<String> deleted = new HashSet<>(flattenedPrev.keySet());
        Map<String, DJsonField> updates = new HashMap<>();
        for (String path : flattenedNext.keySet()) {
            deleted.remove(path);

            DJsonField prev = flattenedPrev.get(path);
            String next = flattenedNext.get(path);

            if (prev == null) {
                updates.put(path, new DJsonField(document, path, next, 1));
            } else if (!next.equals(prev.getJson())) {
                int version = prev.getVersion() + 1;
                updates.put(path, new DJsonField(document, path, next, version));
            }
        }
        for (String path : deleted) {
            DJsonField prev = flattenedPrev.get(path);
            int version = prev.getVersion() + 1;
            updates.put(path, new DJsonField(document, path, null, version));
        }
        return new HashSet<>(updates.values());
    }

    private static Map<String, String> flatten(JsonObject json) {
        return flatten(json, null);
    }

    private static Map<String, String> flatten(JsonObject json, @Nullable String path) {
        Gson gson = JsonUpdateApi.gson();

        if (json.keySet().isEmpty())
            return Map.of(Objects.requireNonNullElse(path, ""), "{}");
        Map<String, String> flattened = new HashMap<>();
        for (String property : json.keySet()) {
            String fieldPath;
            if (path == null) fieldPath = property;
            else fieldPath = path + SEPARATOR + property;

            JsonElement field = json.get(property);
            if (field.isJsonObject())
                flattened.putAll(flatten(field.getAsJsonObject(), fieldPath));
            else flattened.put(fieldPath, gson.toJson(field));
        }
        return flattened;
    }

}
