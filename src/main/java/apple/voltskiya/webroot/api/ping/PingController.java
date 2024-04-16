package apple.voltskiya.webroot.api.ping;


import io.javalin.http.Context;

public class PingController {

    public static void ping(Context ctx) {
        ctx.result("Pong! :D");
    }

}
