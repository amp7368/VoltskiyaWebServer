package apple.voltskiya.webroot.app;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.WebModule;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;
import java.io.File;
import java.util.Set;

public class AdminApp extends WebApp {

    @Override
    protected int getPort() {
        return WebConfig.get().adminPort;
    }

    @Override
    protected File getParentFile() {
        return WebModule.get().getFile("Admin");
    }

    @Override
    protected void additionalConfig(JavalinConfig config) {
        config.accessManager(this::access);
    }

    private void access(Handler handler, Context ctx, Set<? extends RouteRole> routeRoles) {
//        MyRole userRole = SessionManager.getUserRole(ctx);
//        if (routeRoles.contains(userRole)) {
//            handler.handle(ctx);
//        } else {
//            ctx.status(401).result("Unauthorized");
//        }
    }
}
