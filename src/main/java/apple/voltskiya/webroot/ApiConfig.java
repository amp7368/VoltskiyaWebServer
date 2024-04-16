package apple.voltskiya.webroot;

import apple.voltskiya.webroot.api.ApiModule;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import org.slf4j.Logger;

public class ApiConfig {

    protected int port = 80;
    protected String clientDomain = "http://localhost:4200";
    protected boolean isSecureSSL = true;


    private static void logger(Context ctx, Float time) {
        Logger logger = ApiModule.get().logger();
        String message = "(%s) -- %s => %d -- %.2fms".formatted(ctx.ip(), ctx.path(), ctx.statusCode(), time);
        if (ctx.statusCode() < 300) {
            logger.info(message);
            return;
        }
        if (ctx.statusCode() < 500) {
            logger.warn(message);
            return;
        }
        logger.error(message + ctx.result());
    }

    public void configure(JavalinConfig config) {
        config.router.treatMultipleSlashesAsSingleSlash = true;
        config.router.ignoreTrailingSlashes = true;
        config.showJavalinBanner = false;
        config.bundledPlugins.enableCors(cors ->
            cors.addRule(rule -> {
                rule.allowHost(clientDomain);
                rule.allowCredentials = true;
            })
        );
        config.requestLogger.http(ApiConfig::logger);
    }

    public int getPort() {
        return this.port;
    }

    public boolean isSecureSSL() {
        return isSecureSSL;
    }

    public String getDomain() {
        return isSecureSSL() ? this.clientDomain : null;
    }
}
