package app.auth;

import app.App;

import javax.servlet.http.Cookie;

public class Auth {

    private final App app;

    private User user;

    public Auth(App app) {
        this.app = app;
        user = initUser();
    }

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

    public boolean guest(){
        return user == null;
    }

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

    public User user(){
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
