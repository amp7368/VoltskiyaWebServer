package apple.voltskiya.webroot.model.auth.permission;

import io.javalin.security.RouteRole;

public enum DPermission implements RouteRole {
    DEVELOPER,
    MANAGER,
    GAMEMASTER
}
