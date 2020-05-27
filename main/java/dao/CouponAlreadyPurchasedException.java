package dao;
@SuppressWarnings("serial")
public class CouponAlreadyPurchasedException extends Exception{
public CouponAlreadyPurchasedException(String message) {
	super(message);
}
}
