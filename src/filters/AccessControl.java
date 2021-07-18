package filters;

import app.App;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/AccessControl")
public class AccessControl implements Filter {

    public AccessControl() {

    }

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		App app = new App((HttpServletRequest) request, (HttpServletResponse) response);

		if (!app.auth().guest()) {
			if (app.auth().user().isManager()){
				chain.doFilter(request, response);
			} else {
				app.abort(403);
			}
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
