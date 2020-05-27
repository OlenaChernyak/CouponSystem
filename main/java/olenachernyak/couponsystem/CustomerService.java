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
import dao.CouponAlreadyPurchasedException;
import dao.CouponNotExistsException;
import dao.NoSuchCompanyException;
import dao.NoSuchCustomerException;
import dao.ZeroCouponAmountException;
import dao.CouponDBDao.CouponCategory;
import facade.AbsFacade;
import facade.AdminFacade;
import facade.CustomerFacade;
import facade.InvalidLoginException;
import facade.LoginType;
import model.Coupon;

@Path("customer")
public class CustomerService {

	@Context
	private HttpServletRequest request;

	/**
	 * Gets CustomerFacade
	 * 
	 * @return CustomerFacade
	 */
	private CustomerFacade getFacade() {
		HttpSession session = request.getSession(false);
		return (CustomerFacade) session.getAttribute(LoginServlet.CUSTOMER_FACADE);
	}

	@GET
	@Path("getCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Coupon getCoupon(@QueryParam("id") long id) {
		try {
			return getFacade().getCoupon(id);
		} catch (SystemMalfunctionException | CouponNotExistsException e) {
			System.out.println("Failed get coupon! " + e.getMessage());
		}
		return null;
	}

	@DELETE
	@Path("purchaseCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String purchaseCoupon(@QueryParam("id") long id) {
		try {
			getFacade().purchaseCoupon(id);
			return "Congratulations. You can use your coupon now!";
		} catch (SystemMalfunctionException | CouponNotExistsException | CouponAlreadyPurchasedException
				| NoSuchCustomerException | ZeroCouponAmountException e) {
			return "Failed to purchase coupon " + e.getMessage();
		}
	}

	@GET
	@Path("getAllCustomerCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Collection<Coupon> getAllCustomerCoupons() {
		try {
			return getFacade().getAllCouponsOfCustomer();
		} catch (SystemMalfunctionException e) {
			System.out.println("Failed get coupons " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@GET
	@Path("getAllCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Collection<Coupon> getAllCoupons() {
		try {
			return getFacade().getAllCoupons();
		} catch (SystemMalfunctionException | CouponNotExistsException e) {
			System.out.println("Failed to get all coupons " + e.getMessage());
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
			System.out.println("Failed to get coupons of this category. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@GET
	@Path("getCouponsBelowPrice")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Collection<Coupon> getCouponsBelowPrice(@QueryParam("price") double price) throws CouponNotExistsException {
		try {
			return getFacade().getCouponsBelowPrice(price);
		} catch (SystemMalfunctionException e) {
			System.out.println("Failed to get coupons. " + e.getMessage());
		}
		return Collections.emptyList();
	}

}
