package app.validator;

import app.App;

import java.io.IOException;

/**
 * Validate the attributes are not equal to empty string.
 */
public class Required extends Validator{

    public Required(App app) {
        super(app);
    }

    public Required(App app, String failLink) {
        super(app, failLink);
    }

    public void validate(String[] attributes) throws IOException {
        for (String attribute: attributes){
            String value = app.request.getParameter(attribute);
            if(value != null){
                if(value.equals("")){
                    app.setError(attribute, "The " + attribute.replace('_', ' ') + " field is required!");
                }
            }
        }
        end(attributes);
    }

}
