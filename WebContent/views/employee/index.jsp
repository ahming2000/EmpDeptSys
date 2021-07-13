<%@page import="app.App" %>
<%@ page import="models.Department" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Employee" %>
<% App app = (App) request.getAttribute("app"); %>
<% @SuppressWarnings("unchecked") ArrayList<Department> departments = (ArrayList<Department>) app.get("departments"); %>
<% @SuppressWarnings("unchecked") ArrayList<Employee> employees = (ArrayList<Employee>) app.get("employees"); %>

<div class="container" id="app">

    <div class="row mb-2">
        <div class="col-12 d-flex justify-content-between">
            <div class="h1">
                Employee Home
            </div>

            <div class="mt-auto">
                <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addEmployeeModal">
                    <i class="fas fa-plus"></i> Add
                </button>
            </div>
        </div>
    </div>

    <form action="<%= app.url("/employee") %>" method="get" id="filterForm">
        <div class="row mb-3">
            <div class="col-12 col-md-4">
                <select name="paginate" id="paginateSelect" class="form-select w-100">
                    <option value="100" <%= app.paramSelected("paginate", "100") %>>
                        100 records per page
                    </option>
                    <option value="500" <%= app.paramSelected("paginate", "500") %>>
                        500 records per page
                    </option>
                    <option value="1000" <%= app.paramSelected("paginate", "1000") %>>
                        1000 records per page
                    </option>
                    <option value="5000" <%= app.paramSelected("paginate", "5000") %>>
                        5000 records per page
                    </option>
                    <option value="10000" <%= app.paramSelected("paginate", "10000") %>>
                        10000 records per page
                    </option>
                </select>
            </div>

            <div class="col-12 col-md-4">
                <select name="department" id="departmentSelect" class="form-select w-100">
                    <option value="">
                        All (<%= app.get("totalEmpCount") %>)
                    </option>

                    <% for (Department department : departments) { %>
                    <option value="<%= department.getId() %>" <%= app.paramSelected("department", department.getId()) %>>
                        <%= department.getDeptName() %> (<%= app.get("empCount_" + department.getDeptName()) %>)
                    </option>
                    <% } %>
                </select>
            </div>

            <div class="col-12 col-md-4 d-flex justify-content-between">
                <div class="flex-grow-1 me-2">
                    <input type="text" class="form-control" name="search" placeholder="Search"
                           value="<%= app.param("search", "") %>">
                </div>

                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search"></i> Search
                </button>
            </div>
        </div>
    </form>

    <div class="row">
        <div class="col-12 d-flex justify-content-center">
            <%= app.paginate((Integer) app.get("maxPage"), new String[]{"paginate", "department", "search"}) %>
        </div>
    </div>

    <div class="row">
        <div class="col-12">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Department</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <tbody>
                <% for (Employee employee : employees) { %>
                <tr>
                    <td>
                        <input type="hidden" class="employeeId" value="<%= employee.getId() %>">
                        <span class="employeeName"><%= employee.getFullName() %></span>
                    </td>

                    <td>
                        <%= app.get("empCurrentDeptName_" + employee.getId()) %>
                    </td>

                    <td>
                        <button type="button" class="btn btn-primary btn-sm me-1"
                                onclick="window.location.href = '<%= app.url("/employee/view?id=" + employee.getId()) %>'">
                            <i class="fas fa-list"></i>
                        </button>
                        <button type="button" class="btn btn-outline-primary btn-sm me-1"
                                onclick="window.location.href = '<%= app.url("/employee/edit?id=" + employee.getId()) %>'">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-sm deleteEmployeeButton me-1" data-bs-toggle="modal" data-bs-target="#deleteEmployeeModal">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row mb-3">
        <div class="col-12 d-flex justify-content-center">
            <%= app.paginate((Integer) app.get("maxPage"), new String[]{"paginate", "department", "search"}) %>
        </div>
    </div>

    <div class="modal fade" id="addEmployeeModal" tabindex="-1" aria-labelledby="addEmployeeModalTitle" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <div class="modal-header">
                    <h5 class="modal-title" id="addEmployeeModalTitle">Add Employee</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form action="<%= app.url("/employee/create") %>" method="post">
                    <div class="modal-body">
                        <table class="table table-borderless">
                            <tbody>
                            <tr>
                                <td class="text-end">
                                    <label for="input_firstName">First Name: </label>
                                </td>
                                <td>
                                    <input type="text" class="form-control <%= app.hasError("first_name") ? "is-invalid" : "" %>"
                                           name="first_name" id="input_firstName" value="<%= app.getOld("first_name") %>">
                                    <span class="invalid-feedback">
                                        <%= app.hasError("first_name") ? app.getError("first_name") : "" %>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-end">
                                    <label for="input_lastName">Last Name: </label>
                                </td>
                                <td>
                                    <input type="text" class="form-control <%= app.hasError("last_name") ? "is-invalid" : "" %>"
                                           name="last_name" id="input_lastName" value="<%= app.getOld("last_name") %>">
                                    <span class="invalid-feedback">
                                        <%= app.hasError("last_name") ? app.getError("last_name") : "" %>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-end">
                                    <label for="input_gender">Gender: </label>
                                </td>
                                <td>
                                    <select name="gender" id="input_gender"
                                            class="form-select <%= app.hasError("gender") ? "is-invalid" : "" %>">
                                        <option value="M" <%= app.getOld("gender").equals("M") ? "selected" : "" %>>Male</option>
                                        <option value="F" <%= app.getOld("gender").equals("F") ? "selected" : "" %>>Female</option>
                                        <option value="O" <%= app.getOld("gender").equals("O") ? "selected" : "" %>>Other</option>
                                    </select>
                                    <span class="invalid-feedback">
                                        <%= app.hasError("gender") ? app.getError("gender") : "" %>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-end">
                                    <label for="input_birthDate">Day of Birth: </label>
                                </td>
                                <td>
                                    <input type="date" class="form-control <%= app.hasError("birth_date") ? "is-invalid" : "" %>"
                                           name="birth_date" id="input_birthDate" value="<%= app.getOld("birth_date") %>">
                                    <span class="invalid-feedback">
                                        <%= app.hasError("birth_date") ? app.getError("birth_date") : "" %>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-end">
                                    <label for="input_hireDate">Hire Date: </label>
                                </td>
                                <td>
                                    <input type="date" class="form-control <%= app.hasError("hire_date") ? "is-invalid" : "" %>"
                                           name="hire_date" id="input_hireDate" value="<%= app.getOld("hire_date") %>">
                                    <span class="invalid-feedback">
                                        <%= app.hasError("hire_date") ? app.getError("hire_date") : "" %>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-end">
                                    <label for="input_department">Department: </label>
                                </td>
                                <td>
                                    <select name="dept_id" id="input_department"
                                            class="form-select <%= app.hasError("dept_id") ? "is-invalid" : "" %>">
                                        <% for (Department department : departments) { %>
                                        <option value="<%= department.getId() %>" <%= app.getOld("dept_id").equals(department.getId()) ? "selected" : "" %>>
                                            <%= department.getDeptName() %>
                                        </option>
                                        <% } %>
                                    </select>
                                    <span class="invalid-feedback">
                                        <%= app.hasError("dept_id") ? app.getError("dept_id") : "" %>
                                    </span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary">Create</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </form>

            </div>
        </div>
    </div>

    <div class="modal fade" id="deleteEmployeeModal" tabindex="-1" aria-labelledby="deleteEmployeeModalTitle" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <div class="modal-header">
                    <h5 class="modal-title" id="deleteEmployeeModalTitle">Delete Employee</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form action="<%= app.url("/employee/delete") %>" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="id" id="toDeleteId" value="">
                        Are you sure that you want to delete <span id="toDeleteName"></span>?<br>
                        <strong>This action cannot be reversed!</strong>
                    </div>

                    <div class="modal-footer">
                        <button type="submit" class="btn btn-danger">Delete</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </form>

            </div>
        </div>
    </div>

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

<script>
    <% if(app.hasError(new String[]{"first_name", "last_name", "gender", "birth_date", "hire_date", "dept_id", "employee_add"})) { %>
    new bootstrap.Modal(document.getElementById('addEmployeeModal')).toggle();
    <% } %>

    <% if(app.hasSession("message")) { %>
    new bootstrap.Toast(document.getElementById('messageToast')).show();
    <% } %>

    $('.deleteEmployeeButton').on("click", function(){
        let currentRow = $(this).closest('tr');
        let id = currentRow.find('.employeeId').val();
        let name = currentRow.find('.employeeName').html();

        $('#toDeleteId').val(id);
        $('#toDeleteName').html(name);
    });

    $('#departmentSelect').on('change', function () {
        $('#filterForm').submit();
    });

    $('#paginateSelect').on('change', function () {
        $('#filterForm').submit();
    });
</script>
