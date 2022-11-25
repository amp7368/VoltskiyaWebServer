package apple.voltskiya.webroot;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import io.javalin.config.JavalinConfig;
import java.io.File;

public class WebConfig {

    private static AppleAJDInst<WebConfig> manager;
    public int apiPort = 8101;
    public int clientPort = 8102;
    public int adminPort = 8103;
    public String masterUsername = null;
    public String masterPassword = null;

    public static void load() {
        manager = AppleAJD.createInst(WebConfig.class, new File(WebPlugin.get().getDataFolder(), "Config.json"),
            FileIOServiceNow.taskCreator());
        manager.loadOrMake();
    }

    public static WebConfig get() {
        return manager.getInstance();
    }

    public static void commonConfig(JavalinConfig config) {
        config.routing.treatMultipleSlashesAsSingleSlash = true;
        config.routing.ignoreTrailingSlashes = true;
        config.showJavalinBanner = false;
        config.plugins.enableCors((cors -> cors.add(it -> it.allowHost("*.voltskiya.com"))));
    }
}
