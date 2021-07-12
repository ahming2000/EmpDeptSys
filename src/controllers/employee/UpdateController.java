package controllers.employee;

import app.App;
import models.Department;
import models.Employee;
import services.DepartmentEmployeeService;
import services.DepartmentService;
import services.EmployeeService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/employee/edit", "/employee/update"})
public class UpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private EmployeeService eService;

    @Inject
    private DepartmentService dService;

    @Inject
    private DepartmentEmployeeService deService;

    public UpdateController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        App app = new App(request, response);

        String id = request.getParameter("id");
        Employee employee = eService.getEmployee(id);

        if (employee == null) {
            app.abort(404);
        } else {
            app.set("employee", employee);

            // Get employee's current department
            app.set("currentDeptId", deService.getCurrentDepartmentId(id));

            // Get all departments
            ArrayList<Department> departments = dService.getAllDepartments();
            app.set("departments", departments);

            app.view("employee/edit", "Edit " + employee.getFirstName() + " " + employee.getLastName());
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        App app = new App(request, response);

        String empId = request.getParameter("id");
        String action = request.getParameter("action");
        app.validator().setFailLink(app.url("/employee/edit?id=" + empId));

        // Perform update
        switch (action) {
            case "profile":
                // Server side validation
                if (!app.hasError()) app.validator().required(new String[]{"first_name", "last_name", "gender", "birth_date"});
                if (!app.hasError()) app.validator().maxCharacter(new String[]{"first_name"}, 14);
                if (!app.hasError()) app.validator().maxCharacter(new String[]{"last_name"}, 16);

                if(!app.hasError()){
                    Employee employee = eService.getEmployee(empId);

                    employee.setFirstName(request.getParameter("first_name"));
                    employee.setLastName(request.getParameter("last_name"));
                    employee.setGender(request.getParameter("gender"));

                    try {
                        java.util.Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birth_date"));
                        employee.setBirthDate(new java.sql.Date(dob.getTime()));
                    } catch (Exception ignored){}

                    // Update employee
                    eService.updateEmployee(employee);

                    // Set successful message
                    app.setSession("message", "Update profile successfully!");

                    app.redirect("/employee/edit?id=" + empId);
                }
                break;

            case "department":
                // Get parameters
                String deptId = request.getParameter("dept_id");
                String retiredResigned = request.getParameter("retiredResigned");

                // Server side validation
				if (!app.hasError()) app.validator().uniqueDeptEmpId(deService, empId, deptId);

                if(!app.hasError()){
                    // Get required value and entity
                    String currentDeptId = deService.getCurrentDepartmentId(empId);
                    Employee employee = eService.getEmployee(empId);
                    Department department = dService.getDepartment(deptId);

                    if (retiredResigned == null) {
                        // If this employee previously is resigned/retired
                        if (currentDeptId.equals("Resigned/Retired")) {
                            deService.addDepartmentEmployee(employee, department); // Add selected department as new department for this employee
                        }

                        // Else, when department selected is different from current department
                        else if (!currentDeptId.equals(deptId)) {
                            deService.updateDepartmentEmployee(empId, new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())); // Set date now to current department toDate
                            deService.addDepartmentEmployee(employee, department); // Add selected department as new department for this employee
                        }
                    } else {
                        // If this employee choose to resign/retire and previously not resigned/retired (prevent bug)
                        if (!currentDeptId.equals("Resigned/Retired")) {
                            deService.updateDepartmentEmployee(empId, new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())); // Set date now to current department toDate
                        }
                    }

                    // Set successful message
                    app.setSession("message", "Update department successfully!");

                    app.redirect("/employee/edit?id=" + empId);
                }
                break;

            default:
                app.redirect("/employee");
        }
    }

}
