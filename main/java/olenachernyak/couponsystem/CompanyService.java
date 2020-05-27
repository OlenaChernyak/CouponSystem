package olenachernyak.couponsystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import common.SystemMalfunctionException;
import dao.CouponDBDao;
import dao.CouponDBDao.CouponCategory;
import dao.CouponExistsException;
import dao.CouponNotExistsException;
import dao.NoSuchCompanyException;
import facade.AbsFacade;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.InvalidLoginException;
import facade.InvalidUpdateException;
import facade.LoginType;
import model.Company;
import model.Coupon;
import remote.RemoteCoupon;

@Path("company")
public class CompanyService {
	
	@Context
	private HttpServletRequest request;
	
	/**
	 * Gets CompanyFacade
	 * @return CompanyFacade
	 */
	private CompanyFacade getFacade() {
		HttpSession session = request.getSession(false);
		return (CompanyFacade) session.getAttribute(LoginServlet.COMPANY_FACADE);
	}

	@POST 
	@Path("createCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String createCoupon(RemoteCoupon coupon) {
		try {
			getFacade().createCoupon(coupon);
			return "Coupon created successfully.";

		} catch (SystemMalfunctionException | CouponExistsException | CouponNotExistsException e) {
			System.out.println("Failed create coupon. " + e.getMessage());
		}
		return "Failed create coupon. ";
	}

	@DELETE 
	@Path("removeCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String removeCoupon(@QueryParam("id")long id) {
		try {
			getFacade().removeCoupon(id);
		} catch (SystemMalfunctionException | CouponNotExistsException e) {
			System.out.println("Failed remove coupon. " + e.getMessage());
		}
		return "Coupon removed succssesfully.";
	}

	@GET 
	@Path("getCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Coupon getCoupon(@QueryParam("id") long couponId) {
		try {
			return getFacade().getCoupon(couponId);
		} catch (SystemMalfunctionException | CouponNotExistsException e) {
			System.out.println("Failde to get coupon. " + e.getMessage());
		}
		return null;
	}

	@PUT
	@Path("updateCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateCoupon(RemoteCoupon coupon) {
		try {
			getFacade().updateCoupon(coupon);
			return "Coupon updated succssesfully. ";
		} catch (SystemMalfunctionException | CouponNotExistsException | InvalidUpdateException e) {
			return "Failed update coupon. " + e.getMessage();
		}

	}

	@GET
	@Path("getCompany")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Company getCompany(@QueryParam("id") long id) {
		try {
			return getFacade().getCompany();
		} catch (Exception e) {
			System.out.println("Failed to get company. " + e.getMessage());
		}
		return null;
	}

	@GET 
	@Path("getCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)	
	public Collection<Coupon> getCoupons() {
		try {
			return getFacade().getAllCoupons();
		} catch (SystemMalfunctionException e) {
			System.out.println("Failed to get all coupons. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@GET 
	@Path("getCouponsByCategory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)

	public Collection<Coupon> getCouponsByCategory(@QueryParam("category") String category) {
		try {
			CouponCategory cat = CouponCategory.valueOf(category);
			return getFacade().getCouponsByCategory(cat);
		} catch (SystemMalfunctionException | CouponNotExistsException e) {
			System.out.println("Failed to get coupons by category. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@GET 
	@Path("getCouponsBelowPrice")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)

	public Collection<Coupon> getCouponsBelowPrice(@QueryParam("price") double price) {

		try {
			return getFacade().getCouponsBelowPrice(price);
		} catch (SystemMalfunctionException e) {
			System.out.println("Failed to get coupons below the price. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@GET 
	@Path("getCouponsBeforeEndDate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)

	public Collection<Coupon> getCouponsBeforeEndDate(@QueryParam("date") String date) {
		try {
			DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate parsedDate = LocalDate.parse(date, dtFormatter);
			return getFacade().getCouponsBeforeEndDate(parsedDate);
		} catch (SystemMalfunctionException e) {
			System.out.println("Failed to get coupons before end date. " + e.getMessage());
		}
		return Collections.emptyList();
	}
	
}
