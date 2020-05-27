package olenachernyak.couponsystem;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.SystemMalfunctionException;
import dao.NoSuchCompanyException;
import facade.AbsFacade;
import facade.InvalidLoginException;
import facade.LoginType;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	// public static final String KEY_FACADE = "key_facade";
	public static final String ADMIN_FACADE = "admin_facade";
	public static final String COMPANY_FACADE = "company_facade";
	public static final String CUSTOMER_FACADE = "customer_facade";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// req.getRequestDispatcher("WEB-INF/login.html").forward(req, resp);
	}

	// get here after the button submit is pressed
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		// Get all the parameters
		String userName = req.getParameter("user");
		String password = req.getParameter("password");
		String loginTypeString = req.getParameter("loginType");// ADMIN, COMPANY, CUSTOMER

		// Convert login type
		LoginType type = LoginType.valueOf(loginTypeString);

		try {
			// Login to get the facade
			AbsFacade facade = AbsFacade.login(userName, password, type);
			// Save the facade in session
			// session.setAttribute(KEY_FACADE, facade);

			// String pagePath;
			String key;
			switch (type) {
			/* Page selection */
			case ADMIN:
				// facadpagePath = "WEB-INF/admin.html";
				key = ADMIN_FACADE;
				break;
			case COMPANY:
				// pagePath = "WEB-INF/company.html";
				key = COMPANY_FACADE;
				break;
			default:/* CUSTOMER */
				// pagePath = "WEB-INF/customer.html";
				key = CUSTOMER_FACADE;
				break;

			}
			session.setAttribute(key, facade);
			// System.out.println("Got it");
			// Send the user to the appropriate page//
		//	req.getRequestDispatcher(pagePath).forward(req, resp);

			// Check this before sending!!!!!!!!!!!!!
			// resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

		} catch (InvalidLoginException | SystemMalfunctionException | NoSuchCompanyException
				| dao.InvalidLoginException e) {
			System.out.println(e.getMessage());
			//resp.sendRedirect(req.getContextPath() + "/login");
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

		}
	}

}
