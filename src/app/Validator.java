package app;

import services.DepartmentEmployeeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple validation class
 */
public class Validator {

    private final App app;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    private String failLink;

    protected Validator(App app, HttpServletRequest request, HttpServletResponse response) {
        this.app = app;
        this.request = request;
        this.response = response;
        this.failLink = request.getRequestURL().toString();
    }

    /**
     * Finish the validation by send redirect to current URL or continue the application by clearing the session (error).
     */
    private void end() throws IOException {
        if (app.hasError()){
            response.sendRedirect(failLink);
        } else {
            request.getSession().invalidate();
        }
    }

    /**
     * Customize fail link to redirect when validate fail.
     */
    public void setFailLink(String failLink){
        this.failLink = failLink;
    }





    /**
     * Validate the attributes are not equal to empty string.
     * @param attributes Attributes to validate.
     */
    public void required(String[] attributes) throws IOException {
        for (String attribute: attributes){
            String value = request.getParameter(attribute);
            if(value != null){
                if(value.equals("")){
                    app.setError(attribute, "The " + attribute.replace('_', ' ') + " field is required!");
                }
            }
        }
        end();
    }

    /**
     * Validate the attributes are not exceed to certain number of character(s).
     * @param attributes Attributes to validate.
     * @param max Maximum number of character allowed.
     */
    public void maxCharacter(String[] attributes, int max) throws IOException {
        for(String attribute: attributes){
            String value = request.getParameter(attribute);
            if(value.length() > max){
                app.setError(attribute, "The " + attribute.replace('_', ' ') + " field must not exceed " + max + " character(s)!");
            }
        }
        end();
    }

    /**
     * Since employee_id and department_id are composite key of table department_employee,
     * this function validate the employee id and department id are not found in the database
     * to make sure the uniqueness of the entity.
     */
    public void uniqueDeptEmpId(DepartmentEmployeeService deService, String empId, String deptId) throws IOException {
        if(deService.isDuplicated(empId, deptId)){
            app.setError("dept_id", "This employee had registered this department previously!");
        }
        end();
    }
}
