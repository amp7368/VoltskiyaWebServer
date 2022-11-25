package apple.voltskiya.webroot.web;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.session.SessionManager;
import com.voltskiya.lib.AbstractModule;

public class WebModule extends AbstractModule {

    private static WebModule instance;
    private ClientApp client;
    private AdminApp admin;

    public WebModule() {
        instance = this;
    }

    public static WebModule get() {
        return instance;
    }

    @Override
    public void init() {
        new SessionManager();
        WebConfig.load();
        client = new ClientApp();
        admin = new AdminApp();
    }

    @Override
    public void enable() {
        client.start();
        admin.start();
    }

    @Override
    public void onDisable() {
        client.disable();
        admin.disable();
    }

    @Override
    public String getName() {
        return "Web";
    }

}
