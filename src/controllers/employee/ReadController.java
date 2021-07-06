package controllers.employee;

import app.App;
import models.Employee;
import services.DepartmentEmployeeService;
import services.DepartmentService;
import services.EmployeeService;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/employee/view")
public class ReadController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private EmployeeService eService;

    @Inject
    private DepartmentService dService;

    @Inject
    private DepartmentEmployeeService deService;

    public ReadController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        App app = new App(request, response);

        String id = request.getParameter("id");
        Employee employee = eService.getEmployee(id);

        if (employee == null) { // If the employee id is not found
            app.abort(404);
        } else {
            // Get involved departments for employee
            employee.setDepartmentEmployees(deService.getDepartmentsInvolved(id));
            app.set("employee", employee);

            // Get employee's current department
            app.set("currentDeptName", deService.getCurrentDepartmentName(id));

            app.view("employee/view", employee.getFirstName() + " " + employee.getLastName());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
