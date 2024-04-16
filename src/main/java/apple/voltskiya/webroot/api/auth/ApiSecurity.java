package apple.voltskiya.webroot.api.auth;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.model.auth.AuthQuery;
import apple.voltskiya.webroot.model.auth.authentication.DAuthToken;
import apple.voltskiya.webroot.model.auth.identity.DUser;
import com.password4j.Password;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import org.jetbrains.annotations.NotNull;

public class ApiSecurity {

    public static final String TOKEN_COOKIE_KEY = "login_token";
    public static final String USER_ATTRIBUTE = "user";

    public static String hashPassword(String password) {
        return Password.hash(password)
            .addRandomSalt()
            .addPepper(WebConfig.get().pepper)
            .withArgon2()
            .getResult();
    }

    @NotNull
    public static DUser findUser(Context ctx) {
        String tokenCookie = ctx.cookie(TOKEN_COOKIE_KEY);
        if (tokenCookie == null) throw new ForbiddenResponse("Auth token not provided");

        DAuthToken token = AuthQuery.findToken(tokenCookie);
        if (token == null) throw new ForbiddenResponse("Invalid Authorization token");
        if (token.isExpired()) throw new ForbiddenResponse("Authorization token is expired");

        DUser user = token.getUser();
        if (user == null) throw new ForbiddenResponse("Token has no identity?");

        ctx.attribute(USER_ATTRIBUTE, user);
        return user;
    }


    public static boolean checkPassword(String hashed, String input) {
        return Password.check(input, hashed).addPepper(WebConfig.get().pepper).withArgon2();
    }

    public static void handle(@NotNull Context ctx) {
        if (ctx.routeRoles().isEmpty()) return;
        DUser user = findUser(ctx);
        if (!user.hasPermissions(ctx.routeRoles())) {
            throw new ForbiddenResponse("Insufficient permissions");
        }
    }

}
