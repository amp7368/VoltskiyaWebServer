package apple.voltskiya.webroot;

import apple.voltskiya.webroot.model.WebDatabaseModule;
import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.AbstractVoltPlugin;
import java.util.Collection;
import java.util.List;

public class WebPlugin extends AbstractVoltPlugin {

    private static WebPlugin instance;

    public WebPlugin() {
        instance = this;
    }

    public static WebPlugin get() {
        return instance;
    }

    @Override
    public void onLoadPre() {
        WebConfig.load();
    }

    @Override
    public Collection<AbstractModule> getModules() {
        return List.of(new WebDatabaseModule());
    }
}
