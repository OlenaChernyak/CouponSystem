package dao;
@SuppressWarnings("serial")
public class NoSuchCustomerException extends Exception{
	public NoSuchCustomerException(String msg) {
		super(msg);
	}
}
