package app.validator;

import app.App;

import java.io.IOException;

/**
 * Validate the attributes are not exceed to certain number of character(s).
 */
public class Max extends Validator{

    public Max(App app) {
        super(app);
    }

    public Max(App app, String failLink) {
        super(app, failLink);
    }

    public void validate(String[] attributes, int max) throws IOException {
        for(String attribute: attributes){
            String value = request.getParameter(attribute);
            if(value.length() > max){
                app.setError(attribute, "The " + attribute.replace('_', ' ') + " field must not exceed " + max + " character(s)!");
            }
        }
        end();
    }

}
