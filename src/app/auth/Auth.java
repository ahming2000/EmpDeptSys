package app.auth;

import app.App;

import javax.servlet.http.Cookie;

/**
 * Simple authentication class.
 */
public class Auth {

    private final App app;

    private User user;

    public Auth(App app) {
        this.app = app;
        user = initUser();
    }

    /**
     * Extract the login user information.
     */
    private User initUser(){
        User user = new User();

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

    /**
     * Return has user login or not.
     */
    public boolean guest(){
        return user == null;
    }

    /**
     * Login the user by storing the information to Cookie. No authenticate verification occurred here.
     */
    public void login(User user){
        this.user = user;

        String line = user.getId() + "," + user.getFirstName() + "," + user.getLastName() + "," + user.isManager() + "," + user.getDeptId();
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

    /**
     * Get authentication information with Cookie which named as "auth".
     */
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

    /**
     * Get the current login user, return null when there is no user login.
     */
    public User user(){
        return this.user;
    }

}
