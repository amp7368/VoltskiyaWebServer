package apple.voltskiya.webroot.web;

import apple.voltskiya.webroot.WebConfig;
import java.io.File;

public class AdminApp extends WebApp {

    @Override
    protected int getPort() {
        return WebConfig.get().adminPort;
    }

    @Override
    protected File getParentFile() {
        return WebModule.get().getFile("Admin");
    }

}
