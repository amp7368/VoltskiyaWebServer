package apple.voltskiya.webroot.api.auth;

import apple.voltskiya.webroot.model.auth.authentication.DAuthToken;
import java.time.Instant;

public class AuthResponse {

    public Instant tokenExpiration;

    public AuthResponse(DAuthToken login) {
        this.tokenExpiration = login.getExpiration();
    }
}
