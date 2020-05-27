package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import db.Schema;
import Connection.ConnectionPool;
import common.CouponException;
import common.ResourseUtils;
import common.SystemMalfunctionException;
import model.Coupon;
import model.Customer;

public class CustomerDBDao implements CustomerDao {

    public CustomerDBDao() {
        try {
            createTable();
        } catch (SystemMalfunctionException e) {
        }
    }

    @Override
    public void createTable() throws SystemMalfunctionException {
        Connection connection = null;
        Statement stmtCreateCustomerTable = null;
        Statement stmtCreateCustomerCouponTable = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            // Create Customer Table
            stmtCreateCustomerTable = connection.createStatement();
            stmtCreateCustomerTable.executeUpdate(Schema.getCreateCustomerTable());

            // Create Customer_Coupon Table
            stmtCreateCustomerCouponTable = connection.createStatement();
            stmtCreateCustomerCouponTable.executeUpdate(Schema.getCreateCustomerCouponTable());

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failed to create a table." + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            try {
                ResourseUtils.close(stmtCreateCustomerTable, stmtCreateCustomerCouponTable);
            } catch (Exception e) {
                throw new SystemMalfunctionException("Failed to create  a table" + e.getMessage());
            }
        }
    }

    @Override
    public void createCustomer(Customer customer) throws SystemMalfunctionException {
        Connection connection = null;
        PreparedStatement stmtCreateCustomer = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();

            stmtCreateCustomer = connection.prepareStatement(Schema.getCreateCustomer(), Statement.RETURN_GENERATED_KEYS);
            stmtCreateCustomer.setString(1, customer.getName());
            stmtCreateCustomer.setString(2, customer.getPassword());

            int affectedRows = stmtCreateCustomer.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting data failed");
            }

            try (ResultSet generatedKeys = stmtCreateCustomer.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating table failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Faild to create a customer!");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtCreateCustomer);
        }

    }

    @Override
    public void removeCustomer(long customerId) throws SystemMalfunctionException, NoSuchCustomerException {
        Connection connection = null;
        PreparedStatement stmtRemoveCustomer = null;

        if (customerId == Customer.NO_ID) {
            throw new NoSuchCustomerException("Failed to delete customer. No such customer. ");
        } else {
            try {
                connection = ConnectionPool.getInstance().getConnection();
                stmtRemoveCustomer = connection.prepareStatement(Schema.getRemoveCustomer());
                stmtRemoveCustomer.setLong(1, customerId);

                int affectedRows = stmtRemoveCustomer.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Deleting data failed");
                }

            } catch (SQLException e) {
                throw new SystemMalfunctionException("Faild to delete a customer!" + e.getMessage());
            } finally {
                ConnectionPool.getInstance().returnConnection(connection);
                ResourseUtils.close(stmtRemoveCustomer);
            }
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws SystemMalfunctionException, NoSuchCustomerException {
        Connection connection = null;
        PreparedStatement stmtUpdateCustomer = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();

            stmtUpdateCustomer = connection.prepareStatement(Schema.getUpdateCustomer());

            stmtUpdateCustomer.setString(1, customer.getName());
            stmtUpdateCustomer.setString(2, customer.getPassword());
            stmtUpdateCustomer.setLong(3, customer.getId());

            int affectedRows = stmtUpdateCustomer.executeUpdate();
            if (affectedRows == 0) {
                throw new NoSuchCustomerException("Updating data failed");
            }

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Faild to update a customer!");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtUpdateCustomer);
        }

    }

    @Override
    public Customer getCustomer(long id) throws SystemMalfunctionException, NoSuchCustomerException {
        Customer customer = null;
        Connection connection = null;
        PreparedStatement stmtGetCustomer = null;


        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtGetCustomer = connection.prepareStatement(Schema.getCustomer());

            stmtGetCustomer.setLong(1, id);

            ResultSet rs = stmtGetCustomer.executeQuery();

            if (rs.first()) {
                customer = resultSetToCustomer(rs);
            } else {
                throw new NoSuchCustomerException("Failed to get customer. No such id obtained");
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("A customer not found!");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtGetCustomer);
        }
        return customer;
    }

    /**
     * Applies and represents values of a customer.
     *
     * @param rs - a cursor.
     * @return - object Customer.
     * @throws SQLException
     */
    private static Customer resultSetToCustomer(ResultSet rs) throws SQLException {

        long id = rs.getLong(1);
        String name = rs.getString(2);
        String password = rs.getString(3);
        return new Customer(id, name, password);
    }

    @Override
    public Collection<Customer> getAllCustomers() throws SystemMalfunctionException, NoSuchCustomerException {
        Collection<Customer> customers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmtGetAllCustomers = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtGetAllCustomers = connection.prepareStatement(Schema.getAllCustomers());

            ResultSet rs = stmtGetAllCustomers.executeQuery();

            while (rs.next()) {
                Customer customer = getCustomer(rs.getLong(1));
                customers.add(customer);
            }

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Mistake in the process");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtGetAllCustomers);
        }

        return customers;
    }

    @Override
    public Set<Coupon> getCoupons(long customerId) throws SystemMalfunctionException {
        //Coupon coupon = new Coupon();
        Set<Coupon> coupons = new HashSet<>();
        Connection connection = null;
        PreparedStatement stmtGetCouponsFromCustomer = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtGetCouponsFromCustomer = connection.prepareStatement(Schema.getCouponsFromTableCustomer());
            stmtGetCouponsFromCustomer.setLong(1, customerId);

            ResultSet rs = stmtGetCouponsFromCustomer.executeQuery();
            while (rs.next()) {
                //coupon.setId(rs.getLong(1));
                coupons.add(resultSetToCoupon(rs));
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("No couopns found!!");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtGetCouponsFromCustomer);
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


    @Override
    public void insertCustomerCoupon(long couponId, long customerId) throws SystemMalfunctionException,
            CouponAlreadyPurchasedException, CouponNotExistsException, NoSuchCustomerException {

        Connection connection = null;
        PreparedStatement stmtInsertIntoCustomerCoupon = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtInsertIntoCustomerCoupon = connection.prepareStatement(Schema.getInsertCustomerCoupon());

            stmtInsertIntoCustomerCoupon.setLong(1, customerId);
            stmtInsertIntoCustomerCoupon.setLong(2, couponId);
            stmtInsertIntoCustomerCoupon.executeUpdate();

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failed insert data. " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtInsertIntoCustomerCoupon);
        }
    }

    @Override
    public Customer login(String name, String password) throws SystemMalfunctionException, InvalidLoginException {
        Connection connection = null;
        PreparedStatement stmtGetCustomerLogin = null;
        Customer customer = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtGetCustomerLogin = connection.prepareStatement(Schema.getCustomerLogin());

           stmtGetCustomerLogin.setString(1, name);
            stmtGetCustomerLogin.setString(2, password);

            ResultSet rs = stmtGetCustomerLogin.executeQuery();
            if (rs.first()) {
                customer = resultSetToCustomer(rs);
            } else {
                throw new InvalidLoginException("Failed to login. Wrong name or password. ");
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failed login. Wrong name ot password. " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtGetCustomerLogin);
        }
        return customer;
    }

}
