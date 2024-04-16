package apple.voltskiya.webroot.api;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.api.auth.ApiSecurity;
import apple.voltskiya.webroot.api.auth.AuthController;
import apple.voltskiya.webroot.api.ping.PingController;
import io.javalin.Javalin;

public class ApiApp {

    private static ApiApp instance;
    public Javalin app;

    public ApiApp() {
        instance = this;
    }

    public static ApiApp get() {
        return instance;
    }

    public void run() {
        app = Javalin.create((config) -> {
            WebConfig.getApi().configure(config);
        });
        app.beforeMatched(ApiSecurity::handle);

        registerControllers();

        app.start(getPort());
    }

    private void registerControllers() {
        new AuthController().register(app);
        app.get("/ping", PingController::ping);
    }

    private int getPort() {
        return WebConfig.getApi().getPort();
    }

    public void disable() {
        app.stop();
    }

    public void start() {
        new Thread(this::run).start();
    }

}
