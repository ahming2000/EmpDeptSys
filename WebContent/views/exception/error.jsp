<%@page import="app.App" %>
<% App app = (App) request.getAttribute("app"); %>

<style>
    #app {
        color: black;
        font-size: xx-large;
        font-weight: bold;
        margin-top: 280px;
    }
</style>

<div class="d-flex justify-content-center align-text-center" id="app">
    <%= app.get("status") %> | <%= app.get("message") %>
</div>