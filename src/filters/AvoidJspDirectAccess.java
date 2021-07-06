package filters;

import app.App;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/AvoidJspDirectAccess")
public class AvoidJspDirectAccess implements Filter {

    public AvoidJspDirectAccess() {

    }

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		App app = new App((HttpServletRequest) request, (HttpServletResponse) response);
		app.abort(403);
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
