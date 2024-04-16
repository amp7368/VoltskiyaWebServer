package apple.voltskiya.webroot.api.auth;

import apple.voltskiya.webroot.api.auth.grant.GrantRequest;
import apple.voltskiya.webroot.api.auth.signup.SignupRequest;
import apple.voltskiya.webroot.api.base.ApiController;
import apple.voltskiya.webroot.model.auth.AuthQuery;
import apple.voltskiya.webroot.model.auth.authentication.AuthTokenGenerator;
import apple.voltskiya.webroot.model.auth.authentication.DAuthToken;
import apple.voltskiya.webroot.model.auth.identity.DUser;
import apple.voltskiya.webroot.model.auth.permission.DPermission;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.security.BasicAuthCredentials;
import org.jetbrains.annotations.NotNull;

public class AuthController extends ApiController {

    public AuthController() {
        super("/auth");
    }

    @Override
    public void register(Javalin app) {
        app.get(this.path("/refresh"), this::refresh);
        app.get(this.path("/login"), this::login);
        app.post(this.path("/signup"), this::signup);
        app.post(this.path("/grant"), this::grant, DPermission.MANAGER);
    }

    public void refresh(Context ctx) {
        @NotNull DUser user = ApiSecurity.findUser(ctx);
        DAuthToken token = AuthTokenGenerator.create(user);
        ctx.json(new AuthResponse(token));
        ctx.cookie(token.cookie());
    }

    private void grant(Context ctx) {
        GrantRequest request = this.checkBodyAndGet(ctx, GrantRequest.class);
        this.checkErrors(ctx, GrantRequest.VALIDATOR.validator().validate(request));
        DUser user = AuthQuery.grant(request);

        String role = user.getRole().getName();
        String username = user.getName();
        GrantResponse response = new GrantResponse(username, role);
        ctx.json(response).status(HttpStatus.OK);
    }

    private void signup(Context ctx) {
        SignupRequest request = this.checkBodyAndGet(ctx, SignupRequest.class);
        this.checkErrors(ctx, SignupRequest.VALIDATOR.validator().validate(request));

        @NotNull DAuthToken login = AuthQuery.signup(request);
        ctx.json(new AuthResponse(login));
        ctx.cookie(login.cookie());
    }

    private void login(Context ctx) {
        BasicAuthCredentials credentials = ctx.basicAuthCredentials();
        if (credentials == null) throw new BadRequestResponse("Login not found in 'Authorization' header");
        @NotNull DAuthToken login = AuthQuery.login(credentials.getUsername(), credentials.getPassword());
        ctx.json(new AuthResponse(login));
        ctx.cookie(login.cookie());
    }
}
