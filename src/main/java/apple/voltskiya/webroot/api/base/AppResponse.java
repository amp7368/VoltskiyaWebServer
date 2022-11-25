package apple.voltskiya.webroot.api.base;

import apple.voltskiya.webroot.session.WebSession;

public class AppResponse {

    public JsonSession session;

    public AppResponse(WebSession session) {
        this.session = new JsonSession(session);
    }
}
