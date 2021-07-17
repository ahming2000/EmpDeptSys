<%@page import="app.App" %>
<% App app = (App) request.getAttribute("app"); %>

<header>
    <nav class="navbar fixed-top navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="<%= app.url("/") %>">Online Registration System</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup"
                    aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarNavAltMarkup">

                <ul class="navbar-nav ms-auto">
                    <% if (app.auth().guest()) { %>

                    <li class="nav-item">
                        <a href="<%= app.url("/login") %>" class="nav-link">
                            Login
                        </a>
                    </li>

                    <% } else { %>

                    <% if (app.auth().user().isManager()) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= app.url("/employee") %>">
                            Employee Manage
                        </a>
                    </li>
                    <% } %>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <%= app.auth().user().getFullName() %>
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                            <li>
                                <a href="<%= app.url("/profile") %>" class="dropdown-item">
                                    My Profile
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="" onclick="event.preventDefault();document.getElementById('logoutForm').submit();">
                                    Logout
                                </a>
                            </li>
                        </ul>
                    </li>

                    <form action="<%= app.url("/logout") %>" method="post" id="logoutForm"></form>

                    <% } %>
                </ul>

            </div>
        </div>
    </nav>
</header>