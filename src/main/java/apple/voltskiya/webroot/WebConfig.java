package apple.voltskiya.webroot;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import io.javalin.config.JavalinConfig;

public class WebConfig {

    private static AppleAJDInst<WebConfig> manager;
    public int clientPort = 8101;
    public int adminPort = 8102;

    public static void load() {
        manager = AppleAJD.createInst(WebConfig.class, WebModule.get().getFile("Config.json"),
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
    }
}
