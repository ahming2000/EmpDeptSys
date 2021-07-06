package controllers.employee;

import app.App;
import models.Department;
import models.Employee;
import services.DepartmentEmployeeService;
import services.DepartmentService;
import services.EmployeeService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/employee/create")
public class CreateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EmployeeService eService;

	@Inject
	private DepartmentService dService;

	@Inject
	private DepartmentEmployeeService deService;

    public CreateController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		App app = new App(request, response);
		app.setError("employee_add", "Toggle Create Employee Modal");
		app.redirect("/employee");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		App app = new App(request, response);

		// Server side validation
		if (!app.hasError()) app.validator().required(new String[]{"first_name", "last_name", "gender", "birth_date", "hire_date", "dept_id"});
		if (!app.hasError()) app.validator().maxCharacter(new String[]{"first_name"}, 14);
		if (!app.hasError()) app.validator().maxCharacter(new String[]{"last_name"}, 16);

		// Perform create
		if (!app.hasError()){
			// Get parameters
			String firstName = request.getParameter("first_name");
			String lastName = request.getParameter("last_name");
			String gender = request.getParameter("gender");
			String birthDate = request.getParameter("birth_date");
			String hireDate = request.getParameter("hire_date");

			// Create new employee
			Employee employee = eService.addEmployee(firstName, lastName, gender, birthDate, hireDate); // Add employee
			Department department = dService.getDepartment(request.getParameter("dept_id")); // Fetch department class
			deService.addDepartmentEmployee(employee, department); // Add department employee

			// Set successful message
			app.setSession("message", "Create new employee successfully!");

			app.redirect("/employee/view?id=" + employee.getId());
        }
	}

}
