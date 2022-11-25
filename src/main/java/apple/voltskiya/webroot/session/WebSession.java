package apple.voltskiya.webroot.session;

import io.javalin.security.RouteRole;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class WebSession {

    public static final WebSession PUBLIC = new WebSession(AppRole.PUBLIC);
    private final AppRole role;
    private Instant expiration = Instant.now().plus(Duration.ofHours(1));
    private final UUID uuid = UUID.randomUUID();


    public WebSession(AppRole role) {
        this.role = role;
    }

    public boolean isAllowed(Set<? extends RouteRole> routeRoles) {
        return routeRoles.contains(role);
    }

    public UUID getToken() {
        return this.uuid;
    }

    public boolean isValid() {
        return expiration.isAfter(Instant.now());
    }

    public Instant getExpiration() {
        return this.expiration;
    }

    public AppRole getRole() {
        return this.role;
    }
}
