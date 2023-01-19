package apple.voltskiya.webroot.api.login;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.api.base.ApiController;
import apple.voltskiya.webroot.session.AppRole;
import apple.voltskiya.webroot.session.SessionManager;
import apple.voltskiya.webroot.session.WebSession;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.security.BasicAuthCredentials;
import org.jetbrains.annotations.NotNull;

public class AuthController extends ApiController {

    public AuthController() {
        super("");
    }

    @Override
    public void register(Javalin app) {
        app.get(this.path("/login"), this::login, AppRole.PUBLIC);
    }


    public void login(@NotNull Context ctx) throws Exception {
        BasicAuthCredentials login = ctx.basicAuthCredentials();
        if (login == null)
            return;
        String username = login.getUsername();
        String password = login.getPassword();
        boolean valid = this.validCredentials(username, password, WebConfig.get().masterUsername, WebConfig.get().masterPassword);
        if (valid) {
            WebSession session = new WebSession(AppRole.ADMIN);
            SessionManager.get().newSession(session);
            ctx.result(stringify(new LoginResponse(session)));
            return;
        }
        throw new ForbiddenResponse("Bad username or password");
    }

    private boolean validCredentials(String username, String password, String masterUsername, String masterPassword) {
        if (username == null || password == null)
            return false;
        return masterUsername.equals(username) && masterPassword.equals(password);
    }
}
