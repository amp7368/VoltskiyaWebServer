package apple.voltskiya.webroot;

import apple.voltskiya.webroot.app.AdminApp;
import apple.voltskiya.webroot.app.ClientApp;
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
