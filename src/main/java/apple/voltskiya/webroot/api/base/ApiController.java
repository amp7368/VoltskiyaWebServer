package apple.voltskiya.webroot.api.base;

import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import java.time.Instant;

public class ApiController {

    private String basePath;

    public ApiController(String basePath) {
        this.basePath = basePath;
    }

    protected String path(String subPath) {
        return this.basePath + subPath;
    }

    public void register(Javalin app) {
    }

    protected String stringify(Object obj) {
        return new GsonBuilder().registerTypeAdapter(Instant.class, new InstantSerializer()).create().toJson(obj);
    }
}
