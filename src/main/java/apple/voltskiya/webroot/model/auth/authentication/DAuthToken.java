package apple.voltskiya.webroot.model.auth.authentication;

import static apple.voltskiya.webroot.api.auth.ApiSecurity.TOKEN_COOKIE_KEY;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.model.BaseEntity;
import apple.voltskiya.webroot.model.auth.identity.DUser;
import io.javalin.http.Cookie;
import io.javalin.http.SameSite;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "auth_token")
public class DAuthToken extends BaseEntity {

    public static final int TOKEN_BYTES = 30;
    private static final Duration TIME_TO_EXPIRE = Duration.ofHours(1);

    @Id
    @Column(columnDefinition = "char(40)")
    private String token;
    @Column
    private Timestamp lastUsed;
    @ManyToOne
    private DUser user;

    public DAuthToken(DUser user, byte[] tokenBytes, Timestamp lastUsed) {
        this.user = user;
        this.token = tokenToWeb(tokenBytes);
        this.lastUsed = lastUsed;
    }

    @NotNull
    private static String tokenToWeb(byte[] tokenBytes) {
        return Base64.getEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public boolean isExpired() {
        return getExpiration().isBefore(Instant.now());
    }

    public Instant getExpiration() {
        return this.lastUsed.toInstant().plus(TIME_TO_EXPIRE);
    }

    public DUser getUser() {
        return user;
    }

    public int getMaxAge() {
        return (int) Duration.between(Instant.now(), getExpiration()).getSeconds();
    }

    public Cookie cookie() {
        Cookie cookie = new Cookie(TOKEN_COOKIE_KEY, this.token);
        cookie.setDomain(WebConfig.getApi().getDomain());
        cookie.setSecure(WebConfig.getApi().isSecureSSL());
        cookie.setHttpOnly(true);
        cookie.setSameSite(SameSite.STRICT);
        cookie.setMaxAge(getMaxAge());
        return cookie;
    }
}
