package apple.voltskiya.webroot.model.site;

import apple.voltskiya.webroot.model.auth.identity.DUser;
import apple.voltskiya.webroot.model.site.query.QDSite;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class SiteApi {

    @Nullable
    public static DSite findSite(String site) {
        return new QDSite().where()
            .name.ieq(site)
            .findOne();
    }


    public static List<DSite> listSites(DUser owner) {
        return new QDSite().findList();
    }

    public static DSite findOrCreate(String name) {
        DSite site = findSite(name);
        if (site != null) return site;
        site = new DSite(name);
        site.save();
        return site;
    }
}
