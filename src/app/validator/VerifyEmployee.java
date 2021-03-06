package app.validator;

import app.App;
import app.auth.User;
import services.DepartmentEmployeeService;
import services.EmployeeService;

import java.io.IOException;

public class VerifyEmployee extends Validator {
    public VerifyEmployee(App app) {
        super(app);
    }

    public VerifyEmployee(App app, String failLink) {
        super(app, failLink);
    }

    /**
     * Validate user which id, first name, last name need to match database employee table's row
     */
    public void validate(EmployeeService eService, DepartmentEmployeeService deService, User user) throws IOException {
        if (!eService.isEmployee(user.getId(), user.getFirstName(), user.getLastName())) {
            app.setError("id", "The information given doesn't match our records!");
        }

        if (deService.getCurrentDepartmentId(user.getId()).equals("Resigned/Retired")) {
            app.setError("id", "The following employee has no access to the system!");
        }
        end();
    }
}
