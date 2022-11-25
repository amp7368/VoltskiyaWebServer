package apple.voltskiya.webroot.session;

import io.javalin.security.RouteRole;

public enum AppRole implements RouteRole {
    PUBLIC,
    GAMEMASTER,
    ADMIN
}
