package app.auth;

import app.App;

import javax.servlet.http.Cookie;
import java.util.Arrays;

public class Auth {

    private final App app;

    private AuthUser user;

    public Auth(App app) {
        this.app = app;
        user = initAuthUser();
    }

    private AuthUser initAuthUser(){
        AuthUser user = new AuthUser();

        Cookie cookie = getCookie();

        if(cookie != null){
            String line = cookie.getValue();
            String[] values = line.split(",");
            user.setId(values[0]);
            user.setFirstName(values[1]);
            user.setLastName(values[2]);
            user.setManager(Boolean.parseBoolean(values[3]));
            user.setDeptId(values[4]);
            return user;
        } else {
            return null;
        }
    }

    public boolean guest(){
        return user == null;
    }

    public void login(AuthUser authUser){
        this.user = authUser;

        String line = authUser.getId() + "," + authUser.getFirstName() + "," + authUser.getLastName() + "," + authUser.isManager() + "," + authUser.getDeptId();
        Cookie cookie = new Cookie("auth", line);

        int AUTH_EXPIRY_DURATION = 60 * 60 * 24; // 24 hours
        cookie.setMaxAge(AUTH_EXPIRY_DURATION);

        app.response.addCookie(cookie);
    }

    public void logout(){
        Cookie cookie = getCookie();
        if(cookie != null){
            cookie.setMaxAge(0);
            app.response.addCookie(cookie);
        }
    }

    public AuthUser user(){
        return this.user;
    }

    private Cookie getCookie(){
        Cookie[] cookies = app.request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie: cookies){
            if(cookie.getName().equals("auth")){
                return cookie;
            }
        }
        return null;
    }


}
