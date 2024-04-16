package apple.voltskiya.webroot.model;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.model.WebDatabase.WebDatabaseConfig;
import apple.voltskiya.webroot.model.auth.AuthQuery;
import apple.voltskiya.webroot.model.system.InitJsonDocuments;
import apple.voltskiya.webroot.model.system.JSONActions;
import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.configs.factory.AppleConfigLike;
import java.util.List;

public class WebDatabaseModule extends AbstractModule {

    private static WebDatabaseModule instance;

    public WebDatabaseModule() {
        instance = this;
    }

    public static WebDatabaseModule get() {
        return instance;
    }

    @Override
    public void init() {
        new WebDatabase();
        if (!WebConfig.get().isConfigured()) {
            logger().error("Please configure WebConfig");
            System.exit(1);
        }
        AuthQuery.createDefaultAdmin();
    }

    @Override
    public void enable() {
        JSONActions.load();
        InitJsonDocuments.loadSitesAsync();
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(WebDatabaseConfig.class, "DatabaseConfig"));
    }

    @Override
    public String getName() {
        return "Database";
    }
}
