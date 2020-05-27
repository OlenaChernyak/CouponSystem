package dao;
@SuppressWarnings("serial")
public class CouponNotExistsException extends Exception {
	public CouponNotExistsException(String message) {
		super(message);
	}

}
