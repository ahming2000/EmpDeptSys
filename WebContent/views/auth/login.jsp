<%@ page import="app.App" %>
<% App app = (App) request.getAttribute("app"); %>

<div class="container" id="app">
    <div class="row">
        <div class="col-8 col-sm-6 col-lg-4 offset-sm-3 offset-lg-4">

            <div class="h1">Login</div>

            <form action="<%= app.url("/login") %>" method="post">
                <div class="mb-3">
                    <label for="input_id" class="form-label">ID</label>
                    <input type="text" class="form-control <%= app.hasError("id") ? "is-invalid" : "" %>" name="id"
                           id="input_id">
                    <span class="invalid-feedback">
                        <%= app.hasError("id") ? app.getError("id") : "" %>
                    </span>
                </div>

                <div class="mb-3">
                    <label for="input_firstName" class="form-label">First Name</label>
                    <input type="text" class="form-control <%= app.hasError("first_name") ? "is-invalid" : "" %>"
                           name="first_name" id="input_firstName">
                    <span class="invalid-feedback">
                        <%= app.hasError("first_name") ? app.getError("first_name") : "" %>
                    </span>
                </div>

                <div class="mb-3">
                    <label for="input_lastName" class="form-label">Last Name</label>
                    <input type="text" class="form-control <%= app.hasError("last_name") ? "is-invalid" : "" %>"
                           name="last_name" id="input_lastName">
                    <span class="invalid-feedback">
                        <%= app.hasError("last_name") ? app.getError("last_name") : "" %>
                    </span>
                </div>

                <div class="text-center">
                    <button type="submit" class="btn btn-primary">
                        Login
                    </button>
                </div>
            </form>
        </div>
    </div>

</div>