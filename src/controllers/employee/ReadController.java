package controllers.employee;

import app.App;
import app.utility.DisplayControl;
import models.DepartmentEmployee;
import models.Employee;
import services.DepartmentEmployeeService;
import services.DepartmentManagerService;
import services.DepartmentService;
import services.EmployeeService;

import java.io.IOException;
import java.util.ArrayList;
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

        // Get parameter value
        String empId = app.param("id", app.auth().user().getId()); // Get auth user's id in default
        Employee employee = eService.getEmployee(empId);

        if (employee == null) { // If the employee id is not found
            app.abort(404);
        } else {
            // Get data needed
            String deptId = deService.getCurrentDepartmentId(empId);
            ArrayList<DepartmentEmployee> deList = deService.getDepartmentsInvolved(empId);
            String currentDeptName = deService.getCurrentDepartmentName(empId);
            boolean isManager = dmService.isManager(empId, deptId);

            // Set data needed
            employee.setDepartmentEmployees(deList);
            app.set("employee", employee);
            app.set("isManager", isManager);
            app.set("currentDeptName", currentDeptName);

            // Setting up display control
            DisplayControl displayControl = new DisplayControl(app.auth().user(), empId, deptId, isManager);
            app.set("canDelete", displayControl.canDelete());

            app.view("employee/view", employee.getFirstName() + " " + employee.getLastName());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
