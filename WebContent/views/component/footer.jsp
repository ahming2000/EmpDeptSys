<%@page import="app.App" %>
<% App app = (App) request.getAttribute("app"); %>

<footer>

</footer>

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

<script>
    <% if(app.hasSession("message")) { %>
    new bootstrap.Toast(document.getElementById('messageToast')).show();
    <% } %>
</script>
