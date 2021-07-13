<%@ page import="app.App" %>
<%@ page import="models.Employee" %>
<%@ page import="models.Department" %>
<%@ page import="java.util.ArrayList" %>
<% App app = (App) request.getAttribute("app"); %>
<% Employee employee = (Employee) app.get("employee"); %>
<% @SuppressWarnings("unchecked") ArrayList<Department> departments = (ArrayList<Department>) app.get("departments"); %>

<div class="container" id="app">
    <div class="row">
        <div class="col-12 col-sm-10 col-lg-8 col-xl-6 offset-sm-1 offset-lg-2 offset-xl-3">

            <form action="<%= app.url("/employee/update") %>" method="post">
                <input type="hidden" name="action" value="profile">
                <input type="hidden" name="id" value="<%= employee.getId() %>">

                <div class="mb-2">
                    <div class="d-flex justify-content-between">
                        <div class="h1">
                            Update Profile
                        </div>

                        <div class="mt-auto">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> Save
                            </button>
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <table class="table table-borderless">
                        <tbody>
                        <tr>
                            <td class="text-end">
                                <label for="input_firstName">
                                    First Name:
                                </label>
                            </td>
                            <td>
                                <input type="text" class="form-control <%= app.hasError("first_name") ? "is-invalid" : "" %>" name="first_name"
                                       id="input_firstName" value="<%= app.hasError() ? app.getOld("first_name") : employee.getFirstName() %>">
                                <span class="invalid-feedback">
                                    <%= app.hasError("first_name") ? app.getError("first_name") : "" %>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-end">
                                <label for="input_lastName">
                                    Last Name:
                                </label>
                            </td>
                            <td>
                                <input type="text" class="form-control <%= app.hasError("last_name") ? "is-invalid" : "" %>" name="last_name"
                                       id="input_lastName" value="<%= app.hasError() ? app.getOld("last_name") : employee.getLastName() %>">
                                <span class="invalid-feedback">
                                    <%= app.hasError("last_name") ? app.getError("last_name") : "" %>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-end">z 
                                <label for="input_gender">
                                    Gender:
                                </label>
                            </td>
                            <td>
                                <select class="form-select <%= app.hasError("gender") ? "is-invalid" : "" %>" name="gender" id="input_gender">
                                    <option value="M" <%= app.hasError() ? (app.getOld("gender").equals("M") ? "selected" : "") : (employee.getGender().equals("M") ? "selected" : "") %>>
                                        Male
                                    </option>
                                    <option value="F" <%= app.hasError() ? (app.getOld("gender").equals("F") ? "selected" : "") : (employee.getGender().equals("F") ? "selected" : "") %>>
                                        Female
                                    </option>
                                    <option value="O" <%= app.hasError() ? (app.getOld("gender").equals("O") ? "selected" : "") : (employee.getGender().equals("O") ? "selected" : "") %>>
                                        Other
                                    </option>
                                </select>
                                <span class="invalid-feedback">
                                    <%= app.hasError("gender") ? app.getError("gender") : "" %>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-end">
                                <label for="input_birthDate">
                                    Date of Birth:
                                </label>
                            </td>
                            <td>
                                <input type="date" class="form-control <%= app.hasError("birth_date") ? "is-invalid" : "" %>"
                                       name="birth_date" id="input_birthDate" value="<%= app.hasError() ? app.getOld("birth_date") : employee.getBirthDate() %>">
                                <span class="invalid-feedback">
                                    <%= app.hasError("birth_date") ? app.getError("birth_date") : "" %>
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </form>

            <form action="<%= app.url("/employee/update") %>" method="post">
                <input type="hidden" name="action" value="department">
                <input type="hidden" name="id" value="<%= employee.getId() %>">

                <div class="mb-2">
                    <div class="d-flex justify-content-between">
                        <div class="h1">
                            Change Department
                        </div>

                        <div class="mt-auto">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> Save
                            </button>
                        </div>
                    </div>
                </div>

                <div class="mb-1">
                    <select class="form-select <%= app.hasError("dept_id") ? "is-invalid" : "" %>" name="dept_id"
                            id="input_deptName" <%= app.get("currentDeptId").equals("Resigned/Retired") ? "disabled" : "" %>>
                        <% for (Department department : departments) { %>
                        <option value="<%= department.getId() %>" <%= app.get("currentDeptId").equals(department.getId()) ? "selected" : "" %>>
                            <%= department.getDeptName() %>
                        </option>
                        <% } %>
                    </select>
                    <span class="invalid-feedback">
                        <%= app.hasError("dept_id") ? app.getError("dept_id") : "" %>
                    </span>
                </div>

                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="retiredResigned" id="hasResignedRetiredCheckBox"
                        <%= app.get("currentDeptId").equals("Resigned/Retired") ? "checked" : "" %>>
                    <label class="form-check-label" for="hasResignedRetiredCheckBox">
                        Resigned/Retired
                    </label>
                </div>
            </form>

            <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
                <div id="messageToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="toast-header">
                        <strong class="me-auto">Message</strong>
                        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                    </div>
                    <div class="toast-body">
                        <% if (app.hasSession("message")) { %>
                            <%= app.getSession("message") %>
                        <% } %>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<script>
    <% if(app.hasSession("message")) { %>
    new bootstrap.Toast(document.getElementById('messageToast')).show();
    <% } %>

    $('#hasResignedRetiredCheckBox').on('click', function () {
        let deptNameSelect = $('#input_deptName');
        if (deptNameSelect.is(':disabled')) {
            deptNameSelect.prop("disabled", false);
        } else {
            deptNameSelect.prop("disabled", true);
        }
    });
</script>