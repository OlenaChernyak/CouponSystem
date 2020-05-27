package dao;
import model.Customer;
import common.CouponException;
import common.SystemMalfunctionException;

import java.util.Collection;

import model.Coupon;
public interface CustomerDao {

	/**
	 * Creates table Customer and table Customer_Coupon.
	 * @throws SystemMalfunctionException
	 */
	void createTable() throws SystemMalfunctionException;

	/**
	 * Inserts a new customer into Database.
	 * @param customer - a customer to insert.
	 * @throws SystemMalfunctionException
	 */
	void createCustomer (Customer customer) throws SystemMalfunctionException;

	/**
	 * Removes customer from Database.
	 * @param customerId - an ID number of a customer to remove.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCustomerException
	 */
	void removeCustomer (long customerId) throws SystemMalfunctionException, NoSuchCustomerException;

	/**
	 * Updates a customer's information.
	 * @param customer - an ID number of a customer to update.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCustomerException
	 */
	void updateCustomer (Customer customer) throws SystemMalfunctionException, NoSuchCustomerException;

	/**
	 * Gets an object Customer from Database.
	 * @param id - an ID number of a customer.
	 * @return - a new customer.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCustomerException
	 */
	Customer getCustomer(long id) throws SystemMalfunctionException, NoSuchCustomerException;

	/**
	 * Gets collection of all customers.
	 * @return - collection of all customers.
	 * @throws SystemMalfunctionException
	 */
	Collection <Customer> getAllCustomers() throws SystemMalfunctionException, NoSuchCustomerException;

	/**
	 * Gets all coupons of a customer.
	 * @param customerId - an ID number of a customer.
	 * @return - a collection of all coupons of a definite customer.
	 * @throws SystemMalfunctionException
	 */
	Collection <Coupon> getCoupons (long customerId) throws SystemMalfunctionException;

	/**
	 * Inserts into Table Customer_Coupon id's of a customer and the coupon.
	 * @param couponId - an ID number of a customer.
	 * @param customerId - an ID number of a coupon.
	 * @throws SystemMalfunctionException
	 * @throws CouponAlreadyPurchasedException
	 * @throws CouponNotExistsException
	 * @throws NoSuchCustomerException
	 */
	void insertCustomerCoupon(long couponId, long customerId) throws SystemMalfunctionException, CouponAlreadyPurchasedException, CouponNotExistsException, NoSuchCustomerException;

	/**
	 * Gets the customer by checking  his name and password.
	 * @param name - name of a customer.
	 * @param Password - password of a customer.
	 * @return - customer.
	 * @throws SystemMalfunctionException
	 * @throws InvalidLoginException
	 */
	Customer login (String name, String Password) throws  SystemMalfunctionException, InvalidLoginException;
	
	
	
}
