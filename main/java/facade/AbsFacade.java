package facade;

import common.SystemMalfunctionException;
import dao.InvalidLoginException;
import dao.NoSuchCompanyException;

public abstract class AbsFacade {
	/**
	 * A function to help for login a user into the system.
	 * 
	 * @param name     name of a user
	 * @param password a password of a user
	 * @param type     User type
	 * @return A matching facade. For Admin LoginType an AdminFacade will be
	 *         returned. Company - companyFacade. Customer - customerFacade.
	 */

	public static AbsFacade login(String name, String password, LoginType type) throws facade.InvalidLoginException, SystemMalfunctionException, NoSuchCompanyException, dao.InvalidLoginException {
		switch (type) {
			case ADMIN:
				return AdminFacade.performLogin(name, password);
			case COMPANY:
				return CompanyFacade.performLogin(name, password);
			case CUSTOMER:
				return CustomerFacade.performLogin(name, password);
			default:
				throw new InvalidLoginException("Login type is not supported.");
		}
	}

}
