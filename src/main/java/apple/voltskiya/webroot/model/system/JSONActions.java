package apple.voltskiya.webroot.model.system;

import apple.utilities.database.concurrent.ConcurrentAJD;
import apple.utilities.database.concurrent.inst.ConcurrentAJDInst;
import apple.voltskiya.webroot.WebPlugin;
import apple.voltskiya.webroot.model.json.DJsonDocument;
import apple.voltskiya.webroot.model.json.JsonFindApi;
import apple.voltskiya.webroot.model.json.JsonUpdateApi;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JSONActions {

    private static ConcurrentAJDInst<JSONActions> manager;
    protected List<String> resetPath = new ArrayList<>();

    public static void load() {
        File file = WebPlugin.get().getRootFile("JSONActions.json");
        manager = ConcurrentAJD.createInst(JSONActions.class, file);
        manager.loadNow().run();
    }

    private void run() {
        for (String path : resetPath) {
            List<DJsonDocument> documents = JsonFindApi.findPrefixedDocument(path);
            for (DJsonDocument document : documents) {
                JsonUpdateApi.deleteDocument(document);
            }
        }
        resetPath = new ArrayList<>();
        manager.save();
    }
}
