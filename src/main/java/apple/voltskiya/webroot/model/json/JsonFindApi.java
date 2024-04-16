package apple.voltskiya.webroot.model.json;

import apple.voltskiya.webroot.model.WebDatabase;
import apple.voltskiya.webroot.model.json.query.QDJsonDocument;
import apple.voltskiya.webroot.model.site.DSite;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;

public class JsonFindApi {

    public static final String FIND_LATEST_FIELDS_QUERY = """
        SELECT DISTINCT key_path,
                        document_id,
                        id,
                        MAX(jf.version) OVER (PARTITION BY jf.key_path)                      AS version,
                        FIRST_VALUE(jf.json) OVER (PARTITION BY jf.key_path ORDER BY jf.version DESC) AS json
        FROM json_field jf
        WHERE document_id = :document_id;
        """;

    public static Map<String, DJsonField> queryCurrentFields(UUID document) {
        return WebDatabase.db().findNative(DJsonField.class, FIND_LATEST_FIELDS_QUERY)
            .setParameter("document_id", document)
            .findList()
            .stream()
            .collect(Collectors.toMap(
                DJsonField::getKeyPath,
                Function.identity()
            ));
    }

    @Nullable
    public static DJsonDocument findDocument(DSite site, String path) {
        return new QDJsonDocument().where()
            .site.eq(site)
            .path.ieq(path)
            .findOne();
    }

    @Nullable
    public static DJsonDocument findDocument(String siteName, String path) {
        return new QDJsonDocument().where()
            .site.name.ieq(siteName)
            .path.ieq(path)
            .findOne();
    }

    public static String path(String path) {
        if (path == null) return null;
        return path.replaceAll("/*$", "")
            .replaceAll("^/*", "");
    }

    public static List<DJsonDocument> findPrefixedDocument(String path) {
        return new QDJsonDocument().where()
            .path.istartsWith(path(path))
            .findList();
    }

    public static List<DJsonDocument> findAllDocuments() {
        return new QDJsonDocument().findList();
    }
}
