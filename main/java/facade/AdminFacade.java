package facade;

import dao.*;
import model.Company;
import model.Coupon;
import model.Customer;

import java.util.Collection;

import common.SystemMalfunctionException;

public class AdminFacade extends AbsFacade {
	private final CouponDao couponDao;
	private final CustomerDao customerDao;
	private final CompanyDao companyDao;

	public AdminFacade(CouponDao couponDao, CustomerDao customerDao, CompanyDao companyDao) {
		this.companyDao = companyDao;
		this.couponDao = couponDao;
		this.customerDao = customerDao;
	}

	/**
	 * Performד login an Admin into the system.
	 * 
	 * @param name     - name of Admin
	 * @param password - password of Admin
	 * @return CouponDBDao, CustomerDBDao, CompanyDBDao
	 * @throws InvalidLoginException
	 */
	public static AdminFacade performLogin(String name, String password) throws InvalidLoginException {
		if ("admin".contentEquals(name) && "1234".contentEquals(password)) {
			return new AdminFacade(new CouponDBDao(), new CustomerDBDao(), new CompanyDBDao());
		} else {
			String msg = String.format("Cannot login as Admin with the name =%s, and password = %s", name, password);
			throw new InvalidLoginException(msg);
		}
	}

	/**
	 * Inserts company into table Company.
	 * 
	 * @param company - company to insert.
	 * @throws SystemMalfunctionException
	 * @throws CompanyAlreadyExistsException
	 */
	public void createCompany(Company company) throws SystemMalfunctionException, CompanyAlreadyExistsException {
		Collection<Company> allCompanies = companyDao.getAllCompanies();
		for (Company c : allCompanies) {
			if (c.getName().equals(company.getName())) {
				throw new CompanyAlreadyExistsException(
						String.format("Company with name = %s already exits", company.getName()));
			}
		}
			companyDao.createCompany(company);		
	}

	/**
	 * Remobes company from Database.
	 * 
	 * @param companyId - an ID number of a company.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCompanyException
	 * @throws CouponNotExistsException
	 */
	public void removeCompany(long companyId)
			throws SystemMalfunctionException, NoSuchCompanyException, CouponNotExistsException {
		// 1. get all company's coupons
		Collection<Coupon> coupons = companyDao.getAllCoupons(companyId);
		// 2.delete company
		companyDao.removeCompany(companyId);
		// 3.delete all company's coupons
		for (Coupon coupon : coupons) {
			try {
				couponDao.removeCoupon(coupon.getId());
			} catch (CouponNotExistsException e) {
			}
		}
	}

	/**
	 * Updates company details in the Database.
	 * 
	 * @param company - company to update.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCompanyException
	 * @throws InvalidUpdateException
	 */
	public void updateCompany(Company company)
			throws SystemMalfunctionException, NoSuchCompanyException, InvalidUpdateException {

		Company c = companyDao.getCompany(company.getId());

		if (c.getName().equals(company.getName())) {
			throw new InvalidUpdateException("Unable to update company");
		}
		companyDao.updateCompany(company);
	}

	/**
	 * Gets all the companies from the Database.
	 * 
	 * @return - collection of companies.
	 * @throws SystemMalfunctionException
	 */
	public Collection<Company> getAllCompanies() throws SystemMalfunctionException {
		return companyDao.getAllCompanies();
	}

	/**
	 * Gets the company from the Database.
	 * 
	 * @param id - an ID of company to get.
	 * @return - company.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCompanyException
	 */
	public Company getCompany(long id) throws SystemMalfunctionException, NoSuchCompanyException {
		return companyDao.getCompany(id);
	}

	/**
	 * Inserts customer into Database.
	 * 
	 * @param customer - customer to insert.
	 * @throws CustomerAlreadyExistsException
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCustomerException
	 */
	public void createCustomer(Customer customer)
			throws CustomerAlreadyExistsException, SystemMalfunctionException, NoSuchCustomerException {
		Collection<Customer> customers = customerDao.getAllCustomers();
		for (Customer c : customers) {
			if (c.getName().equals(customer.getName())) {
				throw new CustomerAlreadyExistsException(
						String.format("Customer with name = %s already exits", customer.getName()));
			}
		}
		customerDao.createCustomer(customer);
	}

	/**
	 * Removes customer from Database.
	 * 
	 * @param id - an ID of a customer to remove.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCustomerException
	 */
	public void removeCustomer(long id) throws SystemMalfunctionException, NoSuchCustomerException {
		customerDao.removeCustomer(id);
	}

	/**
	 * Updates customer in database, without changing the name of the customer
	 * 
	 * @param customer - the customer to update
	 * @throws NoSuchCustomerException
	 * @throws SystemMalfunctionException
	 * @throws CustomerAlreadyExistsException
	 */
	public void updateCustomer(Customer customer)
			throws SystemMalfunctionException, NoSuchCustomerException, CustomerAlreadyExistsException {
		// The customer already has an ID

		Customer c = customerDao.getCustomer(customer.getId());

		if (c.getName().equals(customer.getName())) {
			throw new CustomerAlreadyExistsException(
					String.format("The customer with this name already exsists", customer.getName()));
		}
		customerDao.updateCustomer(customer);
	}

	/**
	 * Gets all customers form the Database.
	 * 
	 * @return - collection of all customers.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCustomerException
	 */
	public Collection<Customer> getAllCustomers() throws SystemMalfunctionException, NoSuchCustomerException {
		return customerDao.getAllCustomers();
	}

	/**
	 * Gets the customer from Database.
	 * 
	 * @param customerId - an ID of a customer to get.
	 * @return - customer.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCustomerException
	 */
	public Customer getCustomer(long customerId) throws SystemMalfunctionException, NoSuchCustomerException {
		return customerDao.getCustomer(customerId);
	}
}
