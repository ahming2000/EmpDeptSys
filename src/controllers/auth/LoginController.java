package controllers.auth;

import app.App;
import app.auth.User;
import app.validator.Required;
import app.validator.VerifyEmployee;
import services.DepartmentEmployeeService;
import services.DepartmentManagerService;
import services.EmployeeService;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private EmployeeService eService;

	@Inject
	private DepartmentEmployeeService deService;

	@Inject
	private DepartmentManagerService dmService;

    public LoginController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		App app = new App(request, response);
		if (app.auth().guest()){
			app.view("auth/login", "Login");
		} else {
			if (app.auth().user().isManager()){
				app.redirect("/employee");
			} else {
				app.redirect("/profile");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		App app = new App(request, response);

		String id = request.getParameter("id");
		User user = new User(id, request.getParameter("first_name"), request.getParameter("last_name"));

		if (!app.hasError()) new Required(app).validate(new String[]{"id", "first_name", "last_name"});
		if (!app.hasError()) new VerifyEmployee(app).validate(eService, deService, user);

		if (!app.hasError()){
			String currentDeptId = deService.getCurrentDepartmentId(id);
			user.setDeptId(currentDeptId);
			user.setManager(dmService.isManager(id, currentDeptId));

			app.auth().login(user);

			app.setSession("message", "Login successfully!");

			if (user.isManager()){
				app.redirect("/employee");
			} else {
				app.redirect("/profile");
			}
		}
	}

}
