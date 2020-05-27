package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import Connection.ConnectionPool;
import common.ResourseUtils;
import common.SystemMalfunctionException;
import db.Schema;
import model.Coupon;

public class CouponDBDao implements CouponDao {

    public CouponDBDao() {
        try {
            createTable();
        } catch (SystemMalfunctionException e) {

        }
    }

    @Override
    public void createTable() throws SystemMalfunctionException {
        Connection connection = null;
        Statement stmtCreateTableCoupon = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtCreateTableCoupon = connection.createStatement();

            stmtCreateTableCoupon.executeUpdate(Schema.getCreateTableCoupon());

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failed to create a table Coupon" + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtCreateTableCoupon);
        }

    }

    @Override
    public void createCoupon(Coupon coupon, long companyId) throws SystemMalfunctionException {
        Connection connection = null;
        PreparedStatement stmtCreateCoupon = null;
        PreparedStatement stmtInsertIntoCompanyCoupon = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtCreateCoupon = connection.prepareStatement(Schema.getCreateCoupon(), Statement.RETURN_GENERATED_KEYS);
            applyCouponValuesOnStatement(stmtCreateCoupon, coupon);
            stmtCreateCoupon.executeUpdate();


            coupon.setId(getGeneratedPrimaryKey(stmtCreateCoupon));

            stmtInsertIntoCompanyCoupon = connection.prepareStatement(Schema.getInsertIntoCompanyCoupon());
            stmtInsertIntoCompanyCoupon.setLong(1, companyId);
            stmtInsertIntoCompanyCoupon.setLong(2, coupon.getId());

            stmtInsertIntoCompanyCoupon.executeUpdate();

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failed create coupon " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtCreateCoupon);
        }

    }

    /**
     * Generates an ID number for coupon.
     *
     * @param stmtCreateCoupon - a statement to execute.
     * @return - ID number.
     * @throws SQLException
     */
    private static long getGeneratedPrimaryKey(PreparedStatement stmtCreateCoupon) throws SQLException {

        long key = -1;
        ResultSet generatedKeys = stmtCreateCoupon.getGeneratedKeys();

        if (generatedKeys.next()) {
            key = generatedKeys.getLong(1);
        }
        return key;
    }

    /**
     * Applies coupon's values on statement.
     *
     * @param stmtCreateCoupon - a statement to execute for coupon.
     * @param coupon
     * @throws SQLException
     */
    private static void applyCouponValuesOnStatement(PreparedStatement stmtCreateCoupon, Coupon coupon)
            throws SQLException {
        stmtCreateCoupon.setString(1, coupon.getTitle());
        stmtCreateCoupon.setDate(2, Date.valueOf(coupon.getStartDate()));
        stmtCreateCoupon.setDate(3, Date.valueOf(coupon.getEndDate()));
        stmtCreateCoupon.setInt(4, coupon.getAmount());
        stmtCreateCoupon.setInt(5, coupon.getCategory());
        stmtCreateCoupon.setString(6, coupon.getMessage());
        stmtCreateCoupon.setDouble(7, coupon.getPrice());
        stmtCreateCoupon.setString(8, coupon.getImageURL());

    }

    @Override
    public void removeCoupon(long couponId) throws SystemMalfunctionException, CouponNotExistsException {
        Connection connection = null;
        PreparedStatement stmtRemoveCoupon = null;

        if (couponId == Coupon.NO_ID) {
            throw new CouponNotExistsException("Failed to remove coupon. Does not exists");
        } else {
            try {
                connection = ConnectionPool.getInstance().getConnection();
                stmtRemoveCoupon = connection.prepareStatement(Schema.getRemoveCoupon());
                stmtRemoveCoupon.setLong(1, couponId);
                stmtRemoveCoupon.executeUpdate();
            } catch (SQLException e) {
                throw new SystemMalfunctionException("Failed to remove coupon " + e.getMessage());
            } finally {
                ConnectionPool.getInstance().returnConnection(connection);
                ResourseUtils.close(stmtRemoveCoupon);
            }
        }

    }

    @Override
    public void updateCoupon(Coupon coupon) throws SystemMalfunctionException, CouponNotExistsException {
        Connection connection = null;
        PreparedStatement stmtUpdateCoupon = null;

        long id = coupon.getId();
        if (id == Coupon.NO_ID) {
            throw new CouponNotExistsException("Failed update. No such coupon with the id ");
        } else {

            try {
                connection = ConnectionPool.getInstance().getConnection();
                stmtUpdateCoupon = connection.prepareStatement(Schema.getUpdateCouponById());
                applyCouponValuesOnStatement(stmtUpdateCoupon, coupon);
                stmtUpdateCoupon.setLong(9, id);
                int affectedRows = stmtUpdateCoupon.executeUpdate();

                if (affectedRows == 0) {
                    throw new CouponNotExistsException("Failed to update coupon. No such coupon.");
                }

            } catch (SQLException e) {
                throw new SystemMalfunctionException("Failed update coupon " + e.getMessage());
            } finally {
                ConnectionPool.getInstance().returnConnection(connection);
                ResourseUtils.close(stmtUpdateCoupon);
            }
        }
    }

    @Override
    public void decrementCouponAmount(long couponId) throws SystemMalfunctionException, ZeroCouponAmountException, CouponNotExistsException {
        Connection connection = null;
        PreparedStatement stmtDecrementCoupon = null;

        if (couponId == 0) {
            throw new CouponNotExistsException("Failed to update. No such coupon.");
        }
        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtDecrementCoupon = connection.prepareStatement(Schema.getDecrementCouponAmountById());
            stmtDecrementCoupon.setLong(1, couponId);
            int affectedRows = stmtDecrementCoupon.executeUpdate();

            if (affectedRows == 0) {
                throw new ZeroCouponAmountException("Failed reduce coupon amount. ");
            }

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failde reduce coupon amount." + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtDecrementCoupon);
        }


    }

    @Override
    public Coupon getCoupon(long couponId) throws SystemMalfunctionException, CouponNotExistsException {
        Connection connection = null;
        PreparedStatement stmtGetCoupon = null;
        Coupon coupon = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtGetCoupon = connection.prepareStatement(Schema.getCouponById());
            stmtGetCoupon.setLong(1, couponId);

            ResultSet rs = stmtGetCoupon.executeQuery();

            if (rs.first()) {
                coupon = resultSetToCoupon(rs);
            } else {
                throw new CouponNotExistsException("Failed. No such coupon found.");
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failed to get coupon." + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtGetCoupon);
        }
        return coupon;
    }

    /**
     * Gets coupon values from Database.
     *
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
    public Collection<Coupon> getAllCoupons() throws SystemMalfunctionException, CouponNotExistsException {
        Connection connection = null;
        PreparedStatement stmtGetAllCoupons = null;
        Collection<Coupon> coupons = new ArrayList<>();
        ResultSet rs = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmtGetAllCoupons = connection.prepareStatement(Schema.getAllCoupons());
            rs = stmtGetAllCoupons.executeQuery();

            while (rs.next()) {
                Coupon coupon = getCoupon(rs.getLong(1));
                coupons.add(coupon);
            }

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failed to get coupons." + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(stmtGetAllCoupons);
            ResourseUtils.close(rs);
        }
        return coupons;
    }

    @Override
    public Collection<Coupon> getCouponsByCategory(int category) throws SystemMalfunctionException, CouponNotExistsException {
        Collection<Coupon> coupons = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmtGetCouponsByCategory = null;
        ResultSet rs = null;

        try {

            connection = ConnectionPool.getInstance().getConnection();
            stmtGetCouponsByCategory = connection.prepareStatement(Schema.getAllCouponsByCategory());
            stmtGetCouponsByCategory.setInt(1, category);

            rs = stmtGetCouponsByCategory.executeQuery();

            while (rs.next()) {
                Coupon coupon = getCoupon(rs.getInt(1));
                coupons.add(coupon);
            }

        } catch (SQLException e) {
            throw new SystemMalfunctionException("Failed to get coupons. No such category. " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            ResourseUtils.close(rs);
            ResourseUtils.close(stmtGetCouponsByCategory);
        }
        return coupons;
    }

    /**
     * Class for coupon's categories.
     */
    public enum CouponCategory {
        TRAVELLING(1), FOOD(2), ELECTRICITY(3), HEALTH(4), SPORTS(5), CAMPING(6), FASHION(7), STUDIES(8);

        private int value;

        private CouponCategory(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
