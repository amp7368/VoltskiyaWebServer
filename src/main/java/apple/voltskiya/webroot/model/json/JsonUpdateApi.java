package apple.voltskiya.webroot.model.json;

import apple.voltskiya.webroot.model.json.util.UpdateJsonService;
import apple.voltskiya.webroot.model.site.DSite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.ebean.Transaction;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import org.jetbrains.annotations.NotNull;


public class JsonUpdateApi {


    public static DJsonDocument createEmpty(DSite site, String path, Transaction transaction) {
        DJsonDocument document = new DJsonDocument(site, JsonFindApi.path(path));
        document.save(transaction);
        return document;
    }

    public static boolean updateDocument(DJsonDocument document, File file, Transaction transaction) throws IOException {
        String jsonString = Files.readString(file.toPath());
        JsonObject jsonObj = gson().fromJson(jsonString, JsonObject.class);
        String jsonPretty = gsonPretty().toJson(jsonObj);
        boolean isEqual = jsonPretty.equals(jsonString);
        updateDocument(document, jsonObj, jsonPretty, transaction);
        return isEqual;
    }

    public static void updateDocument(DJsonDocument document, JsonObject jsonObj, String jsonString, Transaction transaction) {
        Set<DJsonField> newFields = UpdateJsonService.update(document, jsonObj);
        if (newFields.isEmpty()) return;

        document.setCurrentJson(jsonString);
        for (DJsonField field : newFields) {
            field.save(transaction);
        }
        document.save(transaction);
    }

    public static Gson gsonPretty() {
        return new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }

    @NotNull
    public static Gson gson() {
        return new Gson();
    }

    public static void deleteDocument(DJsonDocument document) {
        document.delete();
        File file = document.getFile();
        if (file.exists()) file.delete();
    }
}
