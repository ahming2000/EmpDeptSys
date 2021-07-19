<%@page import="app.App" %>
<%@page import="models.Employee" %>
<%@page import="models.DepartmentEmployee" %>
<% App app = (App) request.getAttribute("app"); %>
<% Employee employee = (Employee) app.get("employee"); %>

<div class="container" id="app">
    <div class="row">
        <div class="col-12 col-sm-10 col-lg-8 col-xl-6 offset-sm-1 offset-lg-2 offset-xl-3">
            <div class="d-flex justify-content-between">
                <div class="h1">
                    Basic Information
                </div>
                <div class="mt-auto">
                    <% if ((boolean) app.get("canEdit")) { %>
                    <% if (app.auth().user().isManager()) { %>
                    <button class="btn btn-outline-primary"
                            onclick="window.location.href = '<%= app.url("/employee/edit?id=" + employee.getId()) %>'">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <% } else { %>
                    <button class="btn btn-outline-primary"
                            onclick="window.location.href = '<%= app.url("/profile/edit") %>'">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <% } %>
                    <% } %>

                    <% if ((boolean) app.get("canDelete")) { %>
                    <button class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteEmployeeModal">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                    <% } %>
                </div>
            </div>

            <table class="table table-borderless">
                <tbody>
                <tr>
                    <td style="width: 175px">Name: </td>
                    <td><%= employee.getFullName() %><%= (boolean) app.get("isManager") ? " (Manager)" : "" %></td>
                </tr>
                <tr>
                    <td>Gender: </td>
                    <td><%= employee.getGenderLabel() %></td>
                </tr>
                <tr>
                    <td>Day of Birth: </td>
                    <td><%= employee.getBirthDate() %></td>
                </tr>
                <tr>
                    <td>Hire Date: </td>
                    <td><%= employee.getHireDate() %></td>
                </tr>
                <tr>
                    <td>Current Department: </td>
                    <td><%= app.get("currentDeptName") %></td>
                </tr>
                </tbody>
            </table>

            <div class="h1">
                Department History
            </div>

            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th scope="row">Department</th>
                    <th scope="row">Start</th>
                    <th scope="row">End</th>
                </tr>
                </thead>
                <tbody>
                <% for (DepartmentEmployee de: employee.getDepartmentEmployees()) { %>
                <tr>
                    <td><%= de.getDepartment().getDeptName() %></td>
                    <td><%= de.getFromDate() %></td>
                    <td><%= de.getToDate().toString().equals("9999-01-01") ? "-" : de.getToDate() %></td>
                </tr>
                <% } %>
                </tbody>
            </table>

        </div>

        <% if ((boolean) app.get("canDelete")) { %>
        <div class="modal fade" id="deleteEmployeeModal" tabindex="-1" aria-labelledby="deleteEmployeeModalTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteEmployeeModalTitle">Delete Employee</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <form action="<%= app.url("/employee/delete") %>" method="post">
                        <div class="modal-body">
                            <input type="hidden" name="id" id="toDeleteId" value="<%= employee.getId() %>">
                            Are you sure that you want to delete <%= employee.getFullName() %>?<br>
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
        <% } %>

    </div>
</div>