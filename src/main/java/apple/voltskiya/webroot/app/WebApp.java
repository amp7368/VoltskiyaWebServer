package apple.voltskiya.webroot.app;

import apple.voltskiya.webroot.WebConfig;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import java.io.File;
import org.jetbrains.annotations.NotNull;

public abstract class WebApp {

    protected Javalin app;

    protected abstract int getPort();

    protected abstract File getParentFile();

    private File getDirFile() {
        File file = new File(this.getParentFile(), "static");
        file.mkdirs();
        return file;
    }

    public Javalin getApp() {
        return this.app;
    }

    @NotNull
    private String getIndex() {
        return new File(getDirFile(), "index.html").getAbsolutePath();
    }

    @NotNull
    private String getDir() {
        return getDirFile().getAbsolutePath();
    }

    private void run() {
        app = Javalin.create(config -> {
            this.additionalConfig(config);
            WebConfig.commonConfig(config);
            config.staticFiles.add(getDir(), Location.EXTERNAL);
            config.spaRoot.addFile("/", getIndex(), Location.EXTERNAL);
        }).start(getPort());
    }

    protected void additionalConfig(JavalinConfig config) {
    }


    public void disable() {
        app.stop();
    }

    public void start() {
        new Thread(this::run).start();
    }
}
