package apple.voltskiya.webroot.api;

import com.voltskiya.lib.AbstractModule;

public class ApiModule extends AbstractModule {

    private ApiApp api;

    @Override
    public void enable() {
        api = new ApiApp();
        api.start();
    }

    @Override
    public void onDisable() {
        api.disable();
    }

    @Override
    public String getName() {
        return "API";
    }
}
