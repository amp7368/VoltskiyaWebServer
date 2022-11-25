package apple.voltskiya.webroot.api.login;

import apple.voltskiya.webroot.api.base.AppResponse;
import apple.voltskiya.webroot.api.base.JsonSession;
import apple.voltskiya.webroot.session.WebSession;

public class LoginResponse extends AppResponse {
    public LoginResponse(WebSession session) {
        super(session);
    }
}
