package facade;

import common.SystemMalfunctionException;
import dao.*;
import dao.InvalidLoginException;
import model.Coupon;
import model.Customer;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class CustomerFacade extends AbsFacade {
    private final Customer customer;
    private final CustomerDao customerDao;
    private final CouponDao couponDao;

    public CustomerFacade(Customer customer, CustomerDao customerDao, CouponDao couponDao) {
        this.customer = customer;
        this.customerDao = customerDao;
        this.couponDao = couponDao;
    }

    /**
     * Performs login of a Customer into a Database.
     *
     * @param name     - name of a customer.
     * @param password - password of a customer.
     * @return customer, customerDBDao, CouponDBDao()
     * @throws SystemMalfunctionException
     * @throws InvalidLoginException
     */
    protected static CustomerFacade performLogin(String name, String password) throws SystemMalfunctionException, InvalidLoginException {
        CustomerDBDao customerDBDao = new CustomerDBDao();
        Customer customer = customerDBDao.login(name, password);
        return new CustomerFacade(customer, customerDBDao, new CouponDBDao());
    }

    /**
     * Performs a process of purchasing a coupon.
     *
     * @param couponId - ID of a coupon to purchase.
     * @throws SystemMalfunctionException
     * @throws CouponNotExistsException
     * @throws CouponAlreadyPurchasedException
     * @throws NoSuchCustomerException
     * @throws ZeroCouponAmountException
     */
    public void purchaseCoupon(long couponId) throws SystemMalfunctionException, CouponNotExistsException, CouponAlreadyPurchasedException, NoSuchCustomerException, ZeroCouponAmountException {
        couponDao.decrementCouponAmount(couponId);
        customerDao.insertCustomerCoupon(couponId, customer.getId());

    }

    /**
     * Gets all coupons of a customer.
     *
     * @return - all coupons of a customer.
     * @throws SystemMalfunctionException
     */
    public Collection<Coupon> getAllCouponsOfCustomer() throws SystemMalfunctionException {
        return customerDao.getCoupons(customer.getId());
    }

    /**
     * Gets all coupons of a Data base.
     *
     * @return - all coupons of a Data base.
     */
    public Collection<Coupon> getAllCoupons() throws SystemMalfunctionException, CouponNotExistsException{
        return  couponDao.getAllCoupons();
    }

    /**
     * Gets all coupons of a definite category.
     *
     * @param category - category of a coupon to find.
     * @return - coupons of the same category.
     * @throws SystemMalfunctionException
     * @throws CouponNotExistsException
     */
    public Collection<Coupon> getCouponsByCategory(CouponDBDao.CouponCategory category) throws SystemMalfunctionException, CouponNotExistsException {
        Collection<Coupon> coupons = Collections.emptyList();
        switch (category) {
            case TRAVELLING:
            case FOOD:
            case HEALTH:
            case SPORTS:
            case CAMPING:
            case FASHION:
            case STUDIES:
            case ELECTRICITY:
                return coupons = couponDao.getCouponsByCategory(category.getValue());
            default:
                throw new CouponNotExistsException("No such category");
        }
    }

    /**
     * Gets all coupons with the price below given.
     *
     * @param price - price to compare with.
     * @return - all coupons with the price below given.
     * @throws SystemMalfunctionException
     */
    public Collection<Coupon> getCouponsBelowPrice(double price) throws SystemMalfunctionException {
        Collection<Coupon> coupons = customerDao.getCoupons(customer.getId());
        Iterator<Coupon> iterator = coupons.iterator();
        while(iterator.hasNext()){
           Coupon coupon =  iterator.next();
           if(coupon.getPrice() > price){
               coupons.remove(coupon);
           }
        }
        return coupons;
    }
}
