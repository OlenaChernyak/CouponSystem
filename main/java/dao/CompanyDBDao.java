package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import Connection.ConnectionPool;
import common.ResourseUtils;
import common.SystemMalfunctionException;
import db.Schema;
import model.Company;
import model.Coupon;

public class CompanyDBDao implements CompanyDao {

	public CompanyDBDao() {
		try {
			createTable();
		} catch (SystemMalfunctionException e) {
		}
	}

	@Override
	public void createTable() throws SystemMalfunctionException {
		Connection connection = null;
		Statement stmtCreateTableCompany = null;
		Statement stmtCreateTableCompanyCoupon = null;

		try {

			connection = ConnectionPool.getInstance().getConnection();
			stmtCreateTableCompany = connection.createStatement();
			stmtCreateTableCompany.executeUpdate(Schema.getCreateTableCompany());
			
			stmtCreateTableCompanyCoupon = connection.createStatement();
			stmtCreateTableCompanyCoupon.executeUpdate(Schema.getCreateTableCompanyCoupon());

		} catch (SQLException e) {
			throw new SystemMalfunctionException("The table is not created." + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			ResourseUtils.close(stmtCreateTableCompany, stmtCreateTableCompanyCoupon);
		}

	}

	@Override
	public void createCompany(Company company) throws SystemMalfunctionException {
		Connection connection = null;
		PreparedStatement stmtCreateCompany = null;

		
		try {
			connection = ConnectionPool.getInstance().getConnection();
			stmtCreateCompany = connection.prepareStatement(Schema.getCreateCompany());
			applyCompanyValuesOnStatement(stmtCreateCompany, company);

			stmtCreateCompany.executeUpdate();

		} catch (SQLException e) {
			throw new SystemMalfunctionException("Failed create a company" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			ResourseUtils.close(stmtCreateCompany);
		}

	}

	/**
	 * Applies company's values on the statement to execute.
	 * @param createCompany - prepared statement to execute.
	 * @param company - a company the values of which to apply.
	 * @throws SQLException
	 */
	private static void applyCompanyValuesOnStatement(PreparedStatement createCompany, Company company)
			throws SQLException {
		createCompany.setString(1, company.getName());
		createCompany.setString(2, company.getPassword());
		createCompany.setString(3, company.getEmail());
	}

	@Override
	public void removeCompany(long id) throws SystemMalfunctionException, NoSuchCompanyException {
		Connection connection = null;
		PreparedStatement stmtRemoveCompany = null;

		if (id == Company.NO_ID) {
			throw new NoSuchCompanyException("Faild to remove company. Invalid id");
		}

		try {
			connection = ConnectionPool.getInstance().getConnection();
			stmtRemoveCompany = connection.prepareStatement(Schema.getRemoveCompany());
			stmtRemoveCompany.setLong(1, id);
			stmtRemoveCompany.executeUpdate();

		} catch (SQLException e) {
			throw new SystemMalfunctionException("Fail to remove a comapny!" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			ResourseUtils.close(stmtRemoveCompany);
		}

	}

	@Override
	public void updateCompany(Company company) throws SystemMalfunctionException, NoSuchCompanyException {
		Connection connection = null;
		PreparedStatement stmtUpdateCompany = null;

		long id = company.getId();
		if (id == Company.NO_ID) {
			throw new NoSuchCompanyException("Faild to update company. No such company. ");
		}
		try {
			connection = ConnectionPool.getInstance().getConnection();
			stmtUpdateCompany = connection.prepareStatement(Schema.getUpdateCompanyById());
			applyCompanyValuesOnStatement(stmtUpdateCompany, company);
			stmtUpdateCompany.setLong(4, id);
			int affectedRows = stmtUpdateCompany.executeUpdate();

			if (affectedRows == 0) {
				throw new NoSuchCompanyException("Failed to update. No such company.");
			}
		} catch (SQLException e) {
			throw new SystemMalfunctionException("Failed to update company" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			ResourseUtils.close(stmtUpdateCompany);
		}
	}

	@Override
	public Company getCompany(long id) throws SystemMalfunctionException, NoSuchCompanyException {
		Connection connection = null;
		PreparedStatement stmtGetCompany = null;
		Company company = null;
		
		try {		    
			connection = ConnectionPool.getInstance().getConnection();
			stmtGetCompany = connection.prepareStatement(Schema.getSelectCompany());
			stmtGetCompany.setLong(1, id);
			
			ResultSet rs = stmtGetCompany.executeQuery();
			
			if (rs.first()) {
				company = resultSetToCompany(rs);
				company.setCoupons(getCompanyCoupons(id, connection));				
			} else {
				throw new NoSuchCompanyException("Failed to update. No company with such ID");
			}
		} catch (SQLException e) {
			throw new SystemMalfunctionException("Failed." + e.getMessage());
		}finally {
			ConnectionPool.getInstance().returnConnection(connection);
			ResourseUtils.close(stmtGetCompany);
		}
		return company;
	}

	/**
	 * Gets all coupons of the company.
	 * @param id - id number of the company.
	 * @param connection - to get connection with Database.
	 * @return - set of all coupons of the company.
	 * @throws SystemMalfunctionException
	 */
	private static Set<Coupon> getCompanyCoupons(long id, Connection connection) throws SystemMalfunctionException {
		PreparedStatement stmstSelectCompanyCoupons = null;
		Set<Coupon> coupons = new HashSet<>();
		try {
		    stmstSelectCompanyCoupons =	connection.prepareStatement(Schema.getSelectCompanyCouponInnerJoinById());
			stmstSelectCompanyCoupons.setLong(1, id);

			ResultSet rs = stmstSelectCompanyCoupons.executeQuery();
			while (rs.next()) {
				coupons.add(resultSetToCoupon(rs));
			}
		} catch (SQLException e) {
			throw new SystemMalfunctionException("Failed to get company's coupons");
		} finally {
			ResourseUtils.close(stmstSelectCompanyCoupons);
		}
		return coupons;
	}

	/**
	 * Gets coupon's values from Database.
	 * @param rs - ResultSet.
	 * @return - coupon.
	 * @throws SQLException
	 */
	private static Coupon resultSetToCoupon(ResultSet rs) throws SQLException {
		Coupon coupon = new Coupon();
		coupon.setId(rs.getLong(1));
		coupon.setTitle(rs.getString(2));
		coupon.setStartDate(rs.getDate(3).toLocalDate());
		coupon.setEndDate(rs.getDate(4).toLocalDate());
		coupon.setAmount(rs.getInt(5));
		coupon.setCategory(rs.getInt(6));
		coupon.setMessage(rs.getString(7));
		coupon.setPrice(rs.getDouble(8));
		coupon.setImageURL(rs.getString(9));
		return coupon;
	}


	/**
	 * Gets company's values from Database.
	 * @param rs - ResultSet.
	 * @return - company.
	 * @throws SQLException
	 */
	private static Company resultSetToCompany(ResultSet rs) throws SQLException {
		long id = rs.getLong(1);
		String name = rs.getString(2);
		String password = rs.getString(3);
		String email = rs.getString(4);
		return new Company(id, name, password, email);
	}

	@Override
	public Collection<Company> getAllCompanies() throws SystemMalfunctionException {
		Connection connection = null;
		Statement stmtGetAllCompanies = null;
		ResultSet rs = null;

		Collection<Company> companies = new ArrayList<>();

		try {
			connection = ConnectionPool.getInstance().getConnection();
			stmtGetAllCompanies = connection.createStatement();

			rs = stmtGetAllCompanies.executeQuery(Schema.getSelectIdAllCompanies());

			while (rs.next()) {
				Company company = getCompany(rs.getLong(1));
				companies.add(company);
			}

		} catch (SQLException | NoSuchCompanyException e) {
			throw new SystemMalfunctionException("Failed to get all companies." + e.getMessage());

		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			ResourseUtils.close(stmtGetAllCompanies);
			ResourseUtils.close(rs);
		}
		return companies;
	}

	@Override
	public Collection<Coupon> getAllCoupons(long companyId) throws SystemMalfunctionException {
		Connection connection = null;
		try {
			connection = ConnectionPool.getInstance().getConnection();
			return getCompanyCoupons(companyId, connection);

		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
		}
	}

	@Override
	public Company login(String name, String password) throws SystemMalfunctionException, InvalidLoginException, NoSuchCompanyException {
		Connection connection = null;
		PreparedStatement stmtGetCompany = null;
		ResultSet rs = null;

		try {
			connection = ConnectionPool.getInstance().getConnection();
			stmtGetCompany = connection.prepareStatement(Schema.getSelectCompanyByNamePassword());

			stmtGetCompany.setString(1, name);
			stmtGetCompany.setString(2, password);

			rs = stmtGetCompany.executeQuery();
			
			if (rs.first()) {
				long companyId = rs.getLong(1);
				return getCompany(companyId);
			} else {
				throw new InvalidLoginException("Invalid login..");
			}
		} catch (SQLException e) {
			throw new InvalidLoginException("Invalid login." + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			ResourseUtils.close(rs);
			ResourseUtils.close(stmtGetCompany);
		}
	}

}
