package olenachernyak.couponsystem;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter {

	 /** Create new SessionFilter */
	public SessionFilter() {
		}
	
	/**
	 * Initializing of a filter
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * Will be invoked when a user is logging in.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		req.getSession(false);// checks here if there is a session. 
		
		HttpSession session = req.getSession(false);
		
		String key = req.getRequestURI().split("/")[3]+"_facade";
		
		if(session==null || session.getAttribute(key)==null) {
			resp.sendRedirect(req.getContextPath()+"/login");
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Must login.");
		}else {
			chain.doFilter(request, response);// this is a next filter in a row.
		}
		

	}

}
