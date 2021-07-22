package app.validator;

import app.App;

import java.io.IOException;

/**
 * Simple validation class
 */
abstract class Validator {

    protected final App app;

    protected String failLink;

    public Validator(App app) {
        this.app = app;
        this.failLink = app.request.getRequestURL().toString();
    }

    public Validator(App app, String failLink){
        this.app = app;
        this.failLink = failLink;
    }

    /**
     * Finish the validation by send redirect to current URL or continue the application by clearing the session (error).
     */
    protected void end() throws IOException {
        if (app.hasError()){
            app.response.sendRedirect(failLink);
        } else {
            app.request.getSession().invalidate();
        }
    }

    /**
     * Finish the validation by add old input and send redirect to current URL or continue the application by clearing the session (error).
     */
    protected void end(String[] attributes) throws IOException {
        for (String attribute: attributes){
            String value = app.request.getParameter(attribute);
            if(value != null){
                app.setOld(attribute, value);
            }
        }

        if (app.hasError()){
            app.response.sendRedirect(failLink);
        } else {
            app.request.getSession().invalidate();
        }
    }

    /**
     * Customize fail link to redirect when validate fail.
     */
    public void setFailLink(String failLink){
        this.failLink = failLink;
    }

}
