package app.validator;

import app.App;
import app.auth.AuthUser;
import services.EmployeeService;

import java.io.IOException;

public class VerifyEmployee extends Validator{
    public VerifyEmployee(App app) {
        super(app);
    }

    public VerifyEmployee(App app, String failLink) {
        super(app, failLink);
    }

    public void validate(EmployeeService eService, AuthUser user) throws IOException {
        if(!eService.isEmployee(user.getId(), user.getFirstName(), user.getLastName())){
            app.setError("id", "The information given doesn't match our records! ");
        }
        end();
    }
}
