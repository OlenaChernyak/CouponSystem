package dao;
import java.util.Collection;

import common.SystemMalfunctionException;
import model.Coupon;

public interface CouponDao {

	/**
	 * Creates table Coupon in Database.
	 * @throws SystemMalfunctionException
	 */
	void createTable() throws SystemMalfunctionException;

	/**
	 * Creates coupon in the Database.
	 * @param coupon - coupon to insert.
	 * @param companyId - an ID number of a company to which coupon belongs.
	 * @throws SystemMalfunctionException
	 */
	void createCoupon(Coupon coupon, long companyId) throws SystemMalfunctionException;

	/**
	 * Removes a coupon from Database.
	 * @param couponId - an ID number of a coupon to remove.
	 * @throws SystemMalfunctionException
	 * @throws CouponNotExistsException
	 */
	void removeCoupon(long couponId) throws SystemMalfunctionException, CouponNotExistsException;

	/**
	 * Updates the coupon information in Database.
	 * @param coupon - coupon to update.
	 * @throws SystemMalfunctionException
	 * @throws CouponNotExistsException
	 */
	void updateCoupon(Coupon coupon) throws SystemMalfunctionException, CouponNotExistsException;

	/**
	 * Decrements the coupon amount in the Database.
	 * @param couponId - a coupon ID number in Database.
	 * @throws SystemMalfunctionException
	 * @throws ZeroCouponAmountException
	 * @throws CouponNotExistsException
	 */
	void decrementCouponAmount(long couponId) throws SystemMalfunctionException, ZeroCouponAmountException, CouponNotExistsException;

	/**
	 * Gets a coupon from Database.
	 * @param couponId - a coupon ID number in Database.
	 * @return - coupon.
	 * @throws SystemMalfunctionException
	 * @throws CouponNotExistsException
	 */
	Coupon getCoupon (long couponId) throws SystemMalfunctionException, CouponNotExistsException;

	/**
	 * Gets all coupons from Database.
	 * @return - collection of all coupons.
	 * @throws SystemMalfunctionException
	 * @throws CouponNotExistsException
	 */
	Collection <Coupon> getAllCoupons() throws SystemMalfunctionException, CouponNotExistsException;

	/**
	 * Gets all coupons of a definite category.
	 * @param category
	 * @return
	 * @throws SystemMalfunctionException
	 * @throws CouponNotExistsException
	 */
	Collection <Coupon> getCouponsByCategory(int category) throws SystemMalfunctionException, CouponNotExistsException;

}
