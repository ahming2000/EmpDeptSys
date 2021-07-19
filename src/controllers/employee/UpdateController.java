package controllers.employee;

import app.App;
import app.utility.DisplayControl;
import app.validator.Max;
import app.validator.Required;
import models.Department;
import models.Employee;
import services.DepartmentEmployeeService;
import services.DepartmentManagerService;
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

@WebServlet(urlPatterns = {"/employee/edit", "/employee/update", "/profile/edit", "/profile/update"})
public class UpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private EmployeeService eService;

    @Inject
    private DepartmentService dService;

    @Inject
    private DepartmentEmployeeService deService;

    @Inject
    private DepartmentManagerService dmService;

    public UpdateController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        App app = new App(request, response);

        String empId = app.param("id", app.auth().user().getId());
        Employee employee = eService.getEmployee(empId);

        if (employee == null) {
            app.abort(404);
        } else {
            String deptId = deService.getCurrentDepartmentId(empId);
            boolean isManager = dmService.isManager(empId, deptId);
            boolean empHasAuthUserDept = deService.isDuplicated(empId, app.auth().user().getDeptId());

            app.set("employee", employee);

            // Get employee's current department
            app.set("currentDeptId", deService.getCurrentDepartmentId(empId));
            app.set("currentDeptName", deService.getCurrentDepartmentName(empId));

            // Check if the auth user is department's manager or not
            app.set("authUserDeptName", dService.getDepartment(app.auth().user().getDeptId()).getDeptName());

            // Get all departments
            ArrayList<Department> departments = dService.getAllDepartments();
            app.set("departments", departments);

            app.set("isManager", isManager);

            DisplayControl displayControl = new DisplayControl(app.auth().user(), empId, deptId, isManager);
            app.set("canEditProfile", displayControl.canEditProfile());
            app.set("canChangeDept", displayControl.canChangeDept(empHasAuthUserDept));
            app.set("canMarkResignedRetired", displayControl.canMarkResignedRetired());

            app.view("employee/edit", "Edit " + employee.getFirstName() + " " + employee.getLastName());
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        App app = new App(request, response);

        String empId = app.param("id", app.auth().user().getId());
        String action = request.getParameter("action");
        String failLink = app.auth().user().isManager() ? app.url("/employee/edit?id=" + empId) : app.url("/profile/edit");

        // Perform update
        switch (action) {
            case "profile":
                // Server side validation
                if (!app.hasError())
                    new Required(app, failLink).validate(new String[]{"first_name", "last_name", "gender", "birth_date"});
                if (!app.hasError()) new Max(app, failLink).validate(new String[]{"first_name"}, 14);
                if (!app.hasError()) new Max(app, failLink).validate(new String[]{"last_name"}, 16);

                if (!app.hasError()) {
                    Employee employee = eService.getEmployee(empId);

                    employee.setFirstName(request.getParameter("first_name"));
                    employee.setLastName(request.getParameter("last_name"));
                    employee.setGender(request.getParameter("gender"));

                    try {
                        java.util.Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birth_date"));
                        employee.setBirthDate(new java.sql.Date(dob.getTime()));
                    } catch (Exception ignored) {
                    }

                    // Update employee
                    eService.updateEmployee(employee);

                    // Set successful message
                    app.setSession("message", "Update profile successfully!");

                    if (app.auth().user().isManager()){
                        app.redirect("/employee/edit?id=" + empId);
                    } else {
                        app.redirect("/profile/edit");
                    }
                }
                break;

            case "department":
                String subAction = request.getParameter("subAction");

                switch (subAction) {
                    case "changeToAuthUserDept":

                        if (!app.auth().user().getDeptId().equals("Resigned/Retired")) {
                            Department currentAuthUserDept = dService.getDepartment(app.auth().user().getDeptId());

                            String currentDeptId = deService.getCurrentDepartmentId(empId);
                            Employee employee = eService.getEmployee(empId);
                            Department department = dService.getDepartment(currentAuthUserDept.getId());

                            // If this employee previously is resigned/retired
                            if (currentDeptId.equals("Resigned/Retired")) {
                                deService.addDepartmentEmployee(employee, department); // Add selected department as new department for this employee
                            }

                            // Else, when department selected is different from current department
                            else if (!currentDeptId.equals(currentAuthUserDept.getId())) {
                                deService.updateDepartmentEmployee(empId, new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())); // Set date now to current department toDate
                                deService.addDepartmentEmployee(employee, department); // Add selected department as new department for this employee
                            }
                        }
                        break;

                    case "markAsResignRetired":
                        if (!app.auth().user().getDeptId().equals("Resigned/Retired")) {
                            deService.updateDepartmentEmployee(empId, new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())); // Set date now to current department toDate
                        }
                        break;

                    default:
                }

                // Set successful message
                app.setSession("message", "Update department successfully!");

                if (app.auth().user().isManager()){
                    app.redirect("/employee/edit?id=" + empId);
                } else {
                    app.redirect("/profile/edit");
                }
                break;

            default:
                app.redirect("/employee");
        }
    }

}
