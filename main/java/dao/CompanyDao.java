package dao;
import java.util.Collection;

import common.SystemMalfunctionException;
import model.Company;
import model.Coupon;

public interface CompanyDao {

	/**
	 * Creates table Company in the Database.
	 * @throws SystemMalfunctionException
	 */
	void createTable() throws SystemMalfunctionException;

	/**
	 * Creates new company in the Database. Inserts the company's details.
	 * @param company - company to insert.
	 * @throws SystemMalfunctionException
	 */
	void createCompany(Company company) throws SystemMalfunctionException;

	/**
	 * Removes company from the Database.
	 * @param id - id number of the company in the database.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCompanyException
	 */
	void removeCompany(long id) throws SystemMalfunctionException, NoSuchCompanyException;


	/**
	 * Updates company information in the Database.
	 * @param company - company to update.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCompanyException
	 */
	void updateCompany(Company company) throws SystemMalfunctionException, NoSuchCompanyException;

	/**
	 * Gets object of type Company.
	 * @param id - id number of the company in the database.
	 * @return - object Company.
	 * @throws SystemMalfunctionException
	 * @throws NoSuchCompanyException
	 */
	Company getCompany(long id) throws SystemMalfunctionException, NoSuchCompanyException;

	/**
	 * Gets all companies in the Database.
	 * @return - collection of all companies.
	 * @throws SystemMalfunctionException
	 */
	Collection<Company> getAllCompanies () throws SystemMalfunctionException;

	/**
	 * Gets all coupons of the company.
	 * @param companyId - id number of the company in the database.
	 * @return - collection of all coupond of the company.
	 * @throws SystemMalfunctionException
	 */
	Collection<Coupon> getAllCoupons(long companyId) throws SystemMalfunctionException;

	/**
	 * Gets the company from Database by checking the name and the password of the company.
	 * @param name - name of the company to get.
	 * @param password - password of the company to get.
	 * @return - company with the given name and password.
	 * @throws SystemMalfunctionException
	 * @throws InvalidLoginException
	 * @throws NoSuchCompanyException
	 */
	Company login(String name, String password) throws SystemMalfunctionException, InvalidLoginException, NoSuchCompanyException;

}
