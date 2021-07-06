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
                    <li class="nav-item">
                        <a class="nav-link" href="<%= app.url("/employee") %>">
                            Employee Home
                        </a>
                    </li>
                </ul>

            </div>
        </div>
    </nav>
</header>