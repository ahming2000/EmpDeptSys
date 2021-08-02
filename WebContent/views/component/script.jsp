<%@ page import="app.App" %>
<% App app = (App) request.getAttribute("app"); %>

<script src="<%= app.url("/js/jquery-3.6.0.min.js") %>"></script>
<script src="<%= app.url("/js/bootstrap.min.js") %>"></script>
<script src="<%= app.url("/js/fontawesome-all.min.js") %>"></script>