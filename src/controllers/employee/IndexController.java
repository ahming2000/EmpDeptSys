package controllers.employee;

import java.io.IOException;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.App;
import app.utility.DisplayControl;
import models.Department;
import models.Employee;
import services.DepartmentEmployeeService;
import services.DepartmentService;
import services.EmployeeService;

@WebServlet(urlPatterns = {"/employee"})
public class IndexController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private EmployeeService eService;

    @Inject
    private DepartmentService dService;

    @Inject
    private DepartmentEmployeeService deService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        App app = new App(request, response);

        // Get parameter value
        int page = app.param("page", 1);
        int paginate = app.param("paginate", 100);
        String search = app.param("search", "");
        String department = app.param("department", "");

        /* Get Supporting Information */
        // Get all departments
        ArrayList<Department> departments = dService.getAllDepartments();
        app.set("departments", departments);

        // Get total employee count
        app.set("empCount_all", eService.getEmployeeCount());

        // Get all departments' employee count
        for (Department dept : departments) {
            app.set("empCount_" + dept.getId(), deService.getEmployeeCount(dept.getId()));
        }

        // Setting up pagination
        int maxPage = (int) Math.ceil((double) eService.getEmployeeCount(search, department) / paginate);
        app.set("maxPage", maxPage);


        /* Get Primary Information */
        // Get filtered employees
        ArrayList<Employee> employees = eService.getEmployees(page, paginate, search, department);
        app.set("employees", employees);

        for (Employee employee : employees) {
            String deptId = deService.getCurrentDepartmentId(employee.getId().toString());
            app.set("deptId_" + employee.getId().toString(), deptId);
        }

        app.view("employee/index", "Employee Manage");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
