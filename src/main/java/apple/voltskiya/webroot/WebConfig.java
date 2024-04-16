package apple.voltskiya.webroot;

import apple.utilities.database.concurrent.ConcurrentAJD;
import java.io.File;

public class WebConfig {

    private static WebConfig instance;
    public String pepper = "pepper";
    public String adminUsername = "username";
    public String adminPassword = "password";
    public ApiConfig api = new ApiConfig();

    public WebConfig() {
        instance = this;
    }

    public static WebConfig get() {
        return instance;
    }

    public static ApiConfig getApi() {
        return get().api;
    }

    public static void load() {
        File file = WebPlugin.get().getRootFile("WebConfig.json");
        ConcurrentAJD.createInst(WebConfig.class, file).loadNow();
    }

    public boolean isConfigured() {
        return !pepper.equals("pepper") &&
            !adminUsername.equals("username") &&
            !adminPassword.equals("password");
    }
}
