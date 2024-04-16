package apple.voltskiya.webroot.model.auth;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.api.auth.grant.GrantRequest;
import apple.voltskiya.webroot.api.auth.signup.SignupRequest;
import apple.voltskiya.webroot.model.WebDatabase;
import apple.voltskiya.webroot.model.auth.authentication.AuthTokenGenerator;
import apple.voltskiya.webroot.model.auth.authentication.DAuthToken;
import apple.voltskiya.webroot.model.auth.authentication.query.QDAuthToken;
import apple.voltskiya.webroot.model.auth.identity.DUser;
import apple.voltskiya.webroot.model.auth.identity.DUserBasicCredentials;
import apple.voltskiya.webroot.model.auth.identity.query.QDUser;
import apple.voltskiya.webroot.model.auth.identity.query.QDUserBasicCredentials;
import apple.voltskiya.webroot.model.auth.permission.DRole;
import io.ebean.DuplicateKeyException;
import io.ebean.Transaction;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AuthQuery {

    @Nullable
    public static DAuthToken findToken(@NotNull String token) {
        return new QDAuthToken().where()
            .token.eq(token)
            .findOne();
    }

    @NotNull
    public static DAuthToken login(String username, String password) {
        DUserBasicCredentials userCredentials = new QDUserBasicCredentials()
            .where().and()
            .username.eq(username)
            .endAnd()
            .findOne();
        boolean badLogin = userCredentials == null || !userCredentials.isValidPassword(password);
        if (badLogin) throw new UnauthorizedResponse("Invalid username and/or password");
        DUser user = userCredentials.getUser();
        return AuthTokenGenerator.create(user);
    }

    @NotNull
    public static DAuthToken signup(SignupRequest request) {
        DUser user = new DUser(request.username);

        DUserBasicCredentials credentials = new DUserBasicCredentials(user, request.username, request.password);

        try (Transaction transaction = WebDatabase.db().beginTransaction()) {
            user.save(transaction);
            credentials.save(transaction);
            transaction.commit();
        } catch (DuplicateKeyException e) {
            throw new ConflictResponse("Username '%s' is taken!".formatted(request.username));
        }
        return AuthTokenGenerator.create(user);
    }

    public static DUser grant(GrantRequest request) {
        DUserBasicCredentials credentials = new QDUserBasicCredentials().username.eq(request.username).findOne();
        if (credentials == null) throw new NotFoundResponse("User '%s' was not found".formatted(request.username));
        List<DRole> roles = request.getRoles();
        if (roles.size() != request.roles.length) {
            String requestedMessage = String.join(",", request.roles);
            String foundMessage = String.join(",", roles.stream().map(DRole::getName).toArray(String[]::new));
            throw new BadRequestResponse("Not all roles [%s] exist. Found [%s]".formatted(requestedMessage, foundMessage));
        }
        try (Transaction transaction = WebDatabase.db().beginTransaction()) {
            DUser user = credentials.getUser();
            roles.forEach(user::setRole);
            user.save(transaction);
            transaction.commit();
            return user;
        } catch (DuplicateKeyException e) {
            throw new ConflictResponse("User '%s' already has at least one of the roles being granted.".formatted(request.username));
        }
    }

    public static void createDefaultAdmin() {
        String username = WebConfig.get().adminUsername;
        String password = WebConfig.get().adminPassword;

        if (new QDUser().where().name.eq(username).exists()) return;

        DUser user = new DUser(username);
        user.setRole(DRole.APPLE);
        DUserBasicCredentials credentials = new DUserBasicCredentials(user, username, password);
        try (Transaction transaction = WebDatabase.db().beginTransaction()) {
            user.save(transaction);
            credentials.save(transaction);
            transaction.commit();
        }
    }
}
