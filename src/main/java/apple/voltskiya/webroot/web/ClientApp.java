package apple.voltskiya.webroot.web;

import apple.voltskiya.webroot.WebConfig;
import java.io.File;

public class ClientApp extends WebApp {

    @Override
    protected int getPort() {
        return WebConfig.get().clientPort;
    }

    @Override
    protected File getParentFile() {
        return WebModule.get().getFile("Client");
    }
}