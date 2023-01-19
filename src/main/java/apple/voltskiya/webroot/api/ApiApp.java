package apple.voltskiya.webroot.api;

import apple.voltskiya.webroot.WebConfig;
import apple.voltskiya.webroot.api.login.AuthController;
import apple.voltskiya.webroot.api.ping.PingController;
import apple.voltskiya.webroot.session.AppRole;
import apple.voltskiya.webroot.session.SessionManager;
import io.javalin.Javalin;

public class ApiApp {

    protected Javalin app;

    public void run() {
        app = Javalin.create((config) -> {
            SessionManager.get().register(config);
            WebConfig.commonConfig(config);
        });
        new AuthController().register(app);
        app.get("/ping", PingController::root, AppRole.PUBLIC);
        app.get("/api", PingController::no, AppRole.PUBLIC);
        app.start(getPort());
    }

    private int getPort() {
        return WebConfig.get().apiPort;
    }

    public void disable() {
        app.stop();
    }

    public void start() {
        new Thread(this::run).start();
    }

}
