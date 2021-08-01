package controllers.employee;

import app.App;
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

@WebServlet("/employee/delete")
public class DeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EmployeeService eService;

	@Inject
	private DepartmentEmployeeService deService;

    public DeleteController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		App app = new App(request, response);
		app.abort(404);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		App app = new App(request, response);

		// Get parameter
		String id = request.getParameter("id");

		// Perform delete
		eService.deleteEmployee(id);
		deService.deleteDepartmentEmployee(id);

		// Set successful message
		app.setSession("message", "Delete employee successfully!");

		app.redirect("/employee");
	}

}
