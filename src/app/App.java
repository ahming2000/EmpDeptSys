package app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class App {

    public final HttpServletRequest request;
    public final HttpServletResponse response;

    // Variable Manager
    private final HashMap<String, Object> variables;
    private final HashMap<String, String> errors;
    private final HashMap<String, Object> sessions;

    /**
     * Application class for a webpage.
     */
    public App(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        // Variable Manager
        variables = new HashMap<>();
        errors = initError();
        sessions = initSession();
    }


    /* Variable Manager */

    /**
     * Set quick get variable.
     *
     * @param name  Variable name.
     * @param value Variable value.
     */
    public void set(String name, Object value) {
        variables.put(name, value);
    }

    /**
     * Quick get the variable.
     *
     * @param name Variable name.
     * @return Variable value.
     */
    public Object get(String name) {
        return variables.get(name);
    }

    private HashMap<String, String> initError() {
        HashMap<String, String> errors = (HashMap<String, String>) request.getSession().getAttribute("error");
        return Objects.requireNonNullElseGet(errors, HashMap::new);
    }

    /**
     * Set error message with label/name.
     *
     * @param attribute Error label/name.
     * @param message   Error message.
     */
    public void setError(String attribute, String message) {
        errors.put(attribute, message);
        request.getSession().setAttribute("error", errors);
    }


    /**
     * Get the error message.
     *
     * @param attribute Error label/name.
     * @return Error message.
     */
    public String getError(String attribute) {
        return errors.get(attribute);
    }

    /**
     * Check have any errors.
     */
    public boolean hasError() {
        return !errors.isEmpty();
    }

    /**
     * Check error is available or not.
     *
     * @param attribute Error label/name
     */
    public boolean hasError(String attribute) {
        return getError(attribute) != null;
    }

    /**
     * Check errors are available or not.
     *
     * @param attributes Errors label/name
     */
    public boolean hasError(String[] attributes) {
        for (String attribute : attributes) {
            if (getError(attribute) != null) return true;
        }
        return false;
    }

    private HashMap<String, Object> initSession() {
        HashMap<String, Object> sessions = (HashMap<String, Object>) request.getSession().getAttribute("session");
        return Objects.requireNonNullElseGet(sessions, HashMap::new);
    }

    /**
     * Set session with label/name.
     *
     * @param name  Session label/name.
     * @param value Session value.
     */
    public void setSession(String name, String value) {
        sessions.put(name, value);
        request.getSession().setAttribute("session", sessions);
    }

    /**
     * Get session with label/name.
     *
     * @param name Session label/name.
     * @return Session value.
     */
    public Object getSession(String name) {
        return sessions.get(name);
    }

    /**
     * Check session is available or not.
     *
     * @param name Session label/name.
     */
    public boolean hasSession(String name) {
        return getSession(name) != null;
    }


    /* Helper Function */

    /**
     * Display the webpage content by combining many JSP components. Clear session storage after displaying.
     *
     * @param viewPath JSP path without '/' in front and at the back.
     * @param title    Title name of the page.
     */
    public void view(String viewPath, String title) throws IOException, ServletException {
        request.setAttribute("title", title);
        request.setAttribute("app", this);

        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/views/layout/head.jsp").include(request, response);
        request.getRequestDispatcher("/views/" + viewPath + ".jsp").include(request, response);
        request.getRequestDispatcher("/views/layout/tail.jsp").include(request, response);
        request.getSession().invalidate();
    }

    /**
     * Webpage exception redirect message pre-set only for 404 and 403.
     *
     * @param status HTTP status code to set.
     */
    public void abort(int status) throws ServletException, IOException {
        switch (status) {
            case 403:
                abort(status, "You Have No Permission");
                break;
            case 404:
                abort(status, "Not Found");
                break;
            default:
                abort(status, "");
        }
    }

    /**
     * Webpage exception redirect.
     *
     * @param status  HTTP status code to set
     * @param message Custom message/title for exception.
     */
    public void abort(int status, String message) throws ServletException, IOException {
        response.setStatus(status);
        set("message", message);
        set("status", status);
        view("exception/error", message);
    }

    /**
     * Redirect to the link dynamically.
     *
     * @param url Redirect dynamically.
     */
    public void redirect(String url) throws IOException {
        response.sendRedirect(url(url));
    }

    /**
     * Auto generate dynamic url for redirecting.
     *
     * @param url Url pattern or directory pattern.
     * @return Dynamic url link.
     */
    public String url(String url) {
        String currentUrlPattern = request.getServletPath();
        StringBuffer requestURL = request.getRequestURL();
        String baseURL = requestURL.substring(0, requestURL.indexOf(currentUrlPattern));
        return baseURL + url;
    }


    /**
     * Get parameter string value with default value support.
     *
     * @param param        GET or POST parameter name.
     * @param defaultValue Default value to return if parameter not found.
     * @return Parameter value.
     */
    public String param(String param, String defaultValue) {
        return request.getParameter(param) == null ? defaultValue : request.getParameter(param);
    }

    /**
     * Get parameter integer value with default value support.
     *
     * @param param        GET or POST parameter name.
     * @param defaultValue Default value to return if parameter not found.
     * @return Parameter value.
     */
    public int param(String param, int defaultValue) {
        String value = request.getParameter(param);
        if (value == null) return defaultValue;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    /**
     * Quick compare parameter value, return "selected" when it is matched.
     *
     * @param param GET or POST parameter name.
     * @param value Value to compare.
     * @return "selected" or "".
     */
    public String paramSelected(String param, String value) {
        return param(param, "").equals(value) ? "selected" : "";
    }

    /**
     * Generate boostrap pagination with previous, next, first and last button.
     *
     * @param maxPage Maximum page for the pagination.
     * @param params  Parameters that wanted to include in pagination button link.
     * @return Pagination HTML.
     */
    public String paginate(int maxPage, String[] params) {
        if (maxPage <= 1) return ""; // Return empty HTML for only one page

        // Extra button count for left and right of the current page button
        int CURRENT_GAP = 5;

        int currentPage = param("page", 1);
        String htmlText = "";
        String link = getUrlParam(params) + "&page="; // Redirect link

        // Header
        htmlText = htmlText.concat("<nav><ul class='pagination'>");

        // First and previous button
        if (currentPage == 1) {
            htmlText = htmlText.concat(getDisabledButton("<<"));
            htmlText = htmlText.concat(getDisabledButton("<"));
        } else {
            htmlText = htmlText.concat(getButton("<<", link + "1", false));
            htmlText = htmlText.concat(getButton("<", link + (currentPage - 1), false));
        }

        // Page button
        for (int i = currentPage - CURRENT_GAP; i <= currentPage + CURRENT_GAP; i++) {
            if (i >= 1 && i <= maxPage) {
                htmlText = htmlText.concat(getButton(Integer.toString(i), link + i, currentPage == i));
            }
        }

        // Last and next button
        if (currentPage == maxPage) {
            htmlText = htmlText.concat(getDisabledButton(">"));
            htmlText = htmlText.concat(getDisabledButton(">>"));
        } else {
            htmlText = htmlText.concat(getButton(">", link + (currentPage + 1), false));
            htmlText = htmlText.concat(getButton(">>", link + maxPage, false));
        }

        // Footer
        htmlText = htmlText.concat("</ul></nav>");

        return htmlText;
    }

    private String getUrlParam(String[] params) {
        String url = "?";
        for (int i = 0; i < params.length; i++) {
            if (i != 0) url = url.concat("&");
            url = url.concat(params[i] + "=");
            String value = request.getParameter(params[i]);
            if (value != null) {
                if (!value.equals("")) {
                    url = url.concat(value);
                }
            }
        }
        return url;
    }

    private String getDisabledButton(String text) {
        return "<li class='page-item disabled'><span class='page-link'>" + text + "</span></li>";
    }

    private String getButton(String text, String link, boolean active) {
        return "<li class='page-item " + (active ? "active" : "") + "'><a class='page-link' href='" + link + "'>" + text + "</a></li>";
    }

}
