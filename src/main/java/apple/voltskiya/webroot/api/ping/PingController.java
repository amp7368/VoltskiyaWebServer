package apple.voltskiya.webroot.api.ping;

import io.javalin.http.Context;

public class PingController {

    public static void root(Context ctx) {
        ctx.result("Pong! :D");
    }

    public static void no(Context ctx) {
        ctx.result("No!");
    }
}
