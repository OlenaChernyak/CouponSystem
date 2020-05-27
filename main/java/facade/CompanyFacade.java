package facade;

import common.SystemMalfunctionException;
import dao.*;
import dao.InvalidLoginException;
import model.Company;
import model.Coupon;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class CompanyFacade extends AbsFacade {
    private final Company company;
    private final CompanyDao companyDao;
    private final CouponDao couponDao;

    public CompanyFacade(Company company, CompanyDao companyDao, CouponDao couponDao) {
        this.company = company;
        this.companyDao = companyDao;
        this.couponDao = couponDao;
    }

    protected static AbsFacade performLogin(String name, String password) throws SystemMalfunctionException, InvalidLoginException, NoSuchCompanyException {
        CompanyDBDao companyDBDao = new CompanyDBDao();
        Company company = companyDBDao.login(name, password);
        return new CompanyFacade(company, companyDBDao, new CouponDBDao());
    }

    /**
     * Creates coupon in Data base. A coupon with existing Title will be rejected on attempt to create.
     *
     * @param coupon -the coupon to create.
     * @throws SystemMalfunctionException
     * @throws CouponExistsException
     */
    public void createCoupon(Coupon coupon) throws SystemMalfunctionException, CouponExistsException, CouponNotExistsException {
        Collection<Coupon> allCoupons = couponDao.getAllCoupons();
        for (Coupon c : allCoupons) {
            if (c.getTitle().equals(coupon.getTitle())) {
                throw new CouponExistsException(String.format("Coupon with title already exists!", coupon.getTitle()));
            } 
        }
        couponDao.createCoupon(coupon, company.getId());
    }

    /**
     * Removes coupon from Data base.
     *
     * @param couponId - ID of the coupon to be removed.
     * @throws SystemMalfunctionException
     * @throws CouponNotExistsException
     */
    public void removeCoupon(long couponId) throws SystemMalfunctionException, CouponNotExistsException {
        couponDao.removeCoupon(couponId);
    }

    /**
     * Gets the coupon information from the Data base.
     *
     * @param couponId - ID of the coupon to get.
     * @return - coupon.
     * @throws SystemMalfunctionException
     * @throws CouponNotExistsException
     */
    public Coupon getCoupon(long couponId) throws SystemMalfunctionException, CouponNotExistsException {
        return couponDao.getCoupon(couponId);
    }

    /**
     * Updates the fields of the coupon. In case the title is not equal will be rejected to update the coupon.
     *
     * @param coupon - coupon to be updated.
     * @throws SystemMalfunctionException
     * @throws CouponNotExistsException
     * @throws InvalidUpdateException
     */
    public void updateCoupon(Coupon coupon) throws SystemMalfunctionException, CouponNotExistsException, InvalidUpdateException {
        Coupon c = getCoupon(coupon.getId());
        if (c.getTitle().equals(coupon.getTitle())) {
            throw new InvalidUpdateException("Con not update coupon's title");
        }           
        couponDao.updateCoupon(coupon);
    }

    /**
     * Gets the company from the Data base.
     *
     * @return - company.
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Gets all coupons of the given company.
     *
     * @return - collection of coupons.
     * @throws SystemMalfunctionException
     */
    public Collection<Coupon> getAllCoupons() throws SystemMalfunctionException {
        return companyDao.getAllCoupons(company.getId());
    }

    /**
     * Gets the coupons be parameter - category.
     *
     * @param category - category of the coupon.
     * @return collection of the coupons of the same category.
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
     * Gets all the coupons below the definite price.
     *
     * @param price - price of the coupons.
     * @return - collection of the coupons of the needed price.
     */
    public Collection<Coupon> getCouponsBelowPrice(double price) throws SystemMalfunctionException {
        Collection<Coupon> coupons = companyDao.getAllCoupons(company.getId());
        Iterator<Coupon> iterator = coupons.iterator();
        while(iterator.hasNext()){
            Coupon coupon = iterator.next();
            if(coupon.getPrice() > price){
                coupons.remove(coupon);
            }
        }
        return coupons;
    }

    /**
     * Gets all coupons with the definite End date.
     *
     * @param date - end date of the coupon.
     * @return - collection of coupons of the needed end date.
     */
    public Collection<Coupon> getCouponsBeforeEndDate(LocalDate date) throws SystemMalfunctionException {
        Collection<Coupon> coupons = companyDao.getAllCoupons(company.getId());
        Iterator<Coupon> iterator = coupons.iterator();
        while(iterator.hasNext()){
            Coupon coupon = iterator.next();
            if(coupon.getEndDate().isBefore(date)){
                coupons.remove(coupon);
            }
        }
        return coupons;
    }
}

