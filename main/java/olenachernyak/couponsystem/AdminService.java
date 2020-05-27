package olenachernyak.couponsystem;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import common.SystemMalfunctionException;
import dao.CouponNotExistsException;
import dao.NoSuchCompanyException;
import dao.NoSuchCustomerException;
import facade.AbsFacade;
import facade.AdminFacade;
import facade.CompanyAlreadyExistsException;
import facade.CustomerAlreadyExistsException;
import facade.InvalidLoginException;
import facade.InvalidUpdateException;
import facade.LoginType;
import model.Company;
import model.Customer;

@Path("admin")
public class AdminService {
	
	@Context
	private HttpServletRequest request;
	
	/**
	 * Gets an AdminFacade
	 * @return AdminFacade
	 */
	private AdminFacade getFacade() {
		HttpSession session = request.getSession(false);
		return (AdminFacade) session.getAttribute(LoginServlet.ADMIN_FACADE);
	}

	@POST 
	@Path("insertNewCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public String insertNewCustomer(Customer customer) {
		try {
			getFacade().createCustomer(customer);

			return "Customer created!";
		} catch (SystemMalfunctionException
				| CustomerAlreadyExistsException | NoSuchCustomerException e) {
			return "Failed create customer!" + e.getMessage();
		}
	}

	@GET 
	@Path("getAllCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	
	public Collection<Customer> getAllCustomers() {

		try {
			return getFacade().getAllCustomers();
		} catch (SystemMalfunctionException | NoSuchCustomerException e) {
			System.out.println("Failed to get all customers. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@GET 
	@Path("getAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Company> getAllCompanies() {
		try {
			return getFacade().getAllCompanies();
		} catch (SystemMalfunctionException e) {
			System.out.println("Failed to get all companies. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@POST 
	@Path("createCompany")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String createCompany(Company company) {
		try {
			getFacade().createCompany(company);
			return "A company inserted";
		} catch (SystemMalfunctionException | CompanyAlreadyExistsException e) {
			return "Failed create company"+e.getMessage();
		}
	}

	@DELETE 
	@Path("removeCompany")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String removeCompany(@QueryParam("id") long id) {
		try {
			getFacade().removeCompany(id);
			return "Company removed seccssesfully!";
		} catch (SystemMalfunctionException | NoSuchCompanyException | CouponNotExistsException e) {
			return "failed remove company" + e.getMessage();
		}
	}

	@POST 
	@Path("updateCompany")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateCompany(Company company) {
		try {
			getFacade().updateCompany(company);
			return "Company updated successfully!";
		} catch (SystemMalfunctionException | NoSuchCompanyException | InvalidUpdateException e) {
			return "Failed update company" + e.getMessage();
		}
	}

	@GET 
	@Path("getCompany")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	
	public Company getCompany(@QueryParam("id") long id) {

		try {
			return getFacade().getCompany(id);
		} catch (SystemMalfunctionException | NoSuchCompanyException e) {
			System.out.println("Failed get company! " + e.getMessage());
		}
		return null;
	}
	
	
	@GET 
	@Path("getCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Customer getCustomer(@QueryParam("id") long id)  {
		try {
			return getFacade().getCustomer(id);
		} catch (SystemMalfunctionException | NoSuchCustomerException e) {
			System.out.println("Failed get customer! " + e.getMessage());
		}
		return null;
		
	}

	@DELETE 
	@Path("removeCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public String removeCustomer(@QueryParam("id") long id) {
		try {			
			getFacade().removeCustomer(id);
		} catch (SystemMalfunctionException | NoSuchCustomerException e) {
			System.out.println(e.getMessage());
		}
		return "Customer removed seccssesfully!";
	}

	@POST 
	@Path("updateCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateCustomer(Customer customer) {
		try {
			getFacade().updateCustomer(customer);
			return "Customer updated succsesfully!";
		} catch (SystemMalfunctionException | NoSuchCustomerException | InvalidUpdateException e) {
			return "Failed update customer! " + e.getMessage();
		}
	}

}