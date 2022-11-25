package apple.voltskiya.webroot.api.base;

import apple.voltskiya.webroot.session.AppRole;
import apple.voltskiya.webroot.session.WebSession;
import java.time.Instant;
import java.util.UUID;

public class JsonSession {

    public UUID sessionToken;
    public Instant expiration;
    public AppRole role;

    public JsonSession(WebSession session) {
        sessionToken = session.getToken();
        expiration = session.getExpiration();
        role = session.getRole();
    }
}
