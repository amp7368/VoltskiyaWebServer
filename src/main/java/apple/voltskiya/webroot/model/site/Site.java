package apple.voltskiya.webroot.model.site;

import apple.voltskiya.webroot.model.json.DJsonDocument;
import java.util.List;

public class Site {

    private String name;
    private final List<String> documents;

    public Site(DSite site) {
        this.name = site.getName();
        this.documents = site.getDocuments().stream()
            .map(DJsonDocument::getPath)
            .toList();
    }
}
