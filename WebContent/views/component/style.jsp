<%@ page import="app.App" %>
<% App app = (App) request.getAttribute("app"); %>

<link href="<%= app.url("/css/bootstrap.min.css") %>" rel="stylesheet">

<style>
    @font-face {
        font-family: "Nunito";
        src: url("<%= app.url("/font/Nunito/Nunito-Regular.ttf") %>") format("truetype");
    }

    body {
        font-family: "Roboto", sans-serif;
    }

    #app {
        margin-top: 80px;
    }
</style>