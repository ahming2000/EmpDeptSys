package app.validator;

import app.App;
import services.DepartmentEmployeeService;

import java.io.IOException;

/**
 * Since employee_id and department_id are composite key of table department_employee,
 * this function validate the employee id and department id are not found in the database
 * to make sure the uniqueness of the entity.
 */
public class UniqueDeptEmpId extends Validator{

    public UniqueDeptEmpId(App app) {
        super(app);
    }

    public UniqueDeptEmpId(App app, String failLink) {
        super(app, failLink);
    }

    public void validate(DepartmentEmployeeService deService, String empId, String deptId) throws IOException {
        if(deService.isDuplicated(empId, deptId)){
            app.setError("dept_id", "This employee had registered this department previously!");
        }
        end();
    }
}
