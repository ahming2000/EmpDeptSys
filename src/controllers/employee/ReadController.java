package controllers.employee;

import app.App;
import app.utility.DisplayControl;
import models.Employee;
import services.DepartmentEmployeeService;
import services.DepartmentManagerService;
import services.DepartmentService;
import services.EmployeeService;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/employee/view", "/profile"})
public class ReadController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private EmployeeService eService;

    @Inject
    private DepartmentService dService;

    @Inject
    private DepartmentEmployeeService deService;

    @Inject
    private DepartmentManagerService dmService;

    public ReadController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        App app = new App(request, response);

        String empId = app.param("id", app.auth().user().getId());
        Employee employee = eService.getEmployee(empId);

        if (employee == null) { // If the employee id is not found
            app.abort(404);
        } else {
            // Get involved departments for employee
            String deptId = deService.getCurrentDepartmentId(empId);
            boolean isManager = dmService.isManager(empId, deptId);

            employee.setDepartmentEmployees(deService.getDepartmentsInvolved(empId));
            app.set("employee", employee);

            app.set("isManager", isManager);

            DisplayControl displayControl = new DisplayControl(app.auth().user(), empId, deptId, isManager);
            app.set("canDelete", displayControl.canDelete());

            // Get employee's current department
            app.set("currentDeptName", deService.getCurrentDepartmentName(empId));

            app.view("employee/view", employee.getFirstName() + " " + employee.getLastName());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
