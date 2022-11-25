package apple.voltskiya.webroot.session;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SessionManager {

    private static SessionManager instance;
    private final Map<UUID, WebSession> sessions = new HashMap<>();

    public SessionManager() {
        instance = this;
    }

    public static SessionManager get() {
        return instance;
    }


    public void register(JavalinConfig config) {
        config.accessManager(this::access);
    }

    private void access(Handler handler, Context ctx, Set<? extends RouteRole> routeRoles) throws Exception {
        WebSession userRole = getSession(ctx);
        if (userRole.isValid() && userRole.isAllowed(routeRoles)) {
            handler.handle(ctx);
        } else {
            ctx.status(401).result("Unauthorized");
        }
    }

    private WebSession getSession(Context ctx) {
        String tokenHeader = ctx.header("authorization");
        if (tokenHeader == null)
            return WebSession.PUBLIC;
        UUID token;
        try {
            token = UUID.fromString(tokenHeader);
        } catch (IllegalArgumentException e) {
            return WebSession.PUBLIC;
        }
        synchronized (sessions) {
            return this.sessions.getOrDefault(token, WebSession.PUBLIC);
        }
    }

    public void newSession(WebSession webSession) {
        synchronized (sessions) {
            this.sessions.put(webSession.getToken(), webSession);
        }
    }
}
