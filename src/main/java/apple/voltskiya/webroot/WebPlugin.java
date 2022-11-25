package apple.voltskiya.webroot;

import com.voltskiya.lib.AbstractModule;
import com.voltskiya.lib.AbstractVoltPlugin;
import java.util.Collection;
import java.util.List;

public class WebPlugin extends AbstractVoltPlugin {

    @Override
    public Collection<AbstractModule> getModules() {
        return List.of(new WebModule());
    }
}
