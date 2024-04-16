package apple.voltskiya.webroot.model;

import apple.voltskiya.webroot.model.auth.AuthQuery;
import apple.voltskiya.webroot.model.auth.authentication.DAuthToken;
import apple.voltskiya.webroot.model.auth.identity.DUser;
import apple.voltskiya.webroot.model.auth.identity.DUserBasicCredentials;
import apple.voltskiya.webroot.model.json.DJsonDocument;
import apple.voltskiya.webroot.model.json.DJsonField;
import apple.voltskiya.webroot.model.json.JsonFindApi;
import apple.voltskiya.webroot.model.json.JsonUpdateApi;
import apple.voltskiya.webroot.model.site.DSite;
import apple.voltskiya.webroot.model.site.SiteApi;
import com.voltskiya.lib.database.VoltskiyaDatabase;
import com.voltskiya.lib.database.config.VoltskiyaDatabaseBaseConfig;
import com.voltskiya.lib.database.config.VoltskiyaMysqlConfig;
import io.ebean.Database;
import io.ebean.config.AutoTuneConfig;
import io.ebean.config.AutoTuneMode;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebDatabase extends VoltskiyaDatabase {

    public static final String NAME = "web_api";
    public static final Object UPDATE_SYNC = new Object();
    private static WebDatabase instance;

    public WebDatabase() {
        instance = this;
    }

    public static WebDatabase get() {
        return instance;
    }

    public static Database db() {
        return get().getDB();
    }

    @Override
    protected void addEntities(List<Class<?>> entities) {
        entities.add(BaseEntity.class);
        // auth
        entities.addAll(List.of(DUser.class, DUserBasicCredentials.class, DAuthToken.class));
        // json
        entities.addAll(List.of(DJsonField.class, DJsonDocument.class));
        // site
        entities.add(DSite.class);
    }

    @Override
    protected Collection<Class<?>> getQueryBeans() {
        return new ArrayList<>(List.of(
            AuthQuery.class,
            JsonFindApi.class,
            JsonUpdateApi.class,
            SiteApi.class
        ));
    }

    @Override
    protected VoltskiyaDatabaseBaseConfig getConfig() {
        return WebDatabaseConfig.get();
    }

    @Override
    protected DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig databaseConfig = super.configureDatabase(dataSourceConfig);
        AutoTuneConfig autoTune = databaseConfig.getAutoTuneConfig();
        autoTune.setProfiling(true);
        autoTune.setQueryTuning(true);
        autoTune.setMode(AutoTuneMode.DEFAULT_ON);
        return databaseConfig;
    }

    @Override
    protected String getName() {
        return NAME;
    }

    public static class WebDatabaseConfig extends VoltskiyaMysqlConfig {

        private static WebDatabaseConfig instance;

        public WebDatabaseConfig() {
            instance = this;
        }

        public static WebDatabaseConfig get() {
            return instance;
        }
    }
}
