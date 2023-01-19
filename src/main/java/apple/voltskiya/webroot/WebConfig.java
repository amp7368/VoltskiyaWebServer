package apple.voltskiya.webroot;

import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import com.voltskiya.lib.pmc.FileIOServiceNow;
import io.javalin.config.JavalinConfig;
import io.javalin.plugin.bundled.CorsPluginConfig;
import java.io.File;

public class WebConfig {

    private static AppleAJDInst<WebConfig> manager;
    public int apiPort = 8102;
    public String masterUsername = "username";
    public String masterPassword = "password";

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
        config.plugins.enableCors((cors -> cors.add(CorsPluginConfig::anyHost)));
    }
}
