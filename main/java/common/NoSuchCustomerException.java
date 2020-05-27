package common;
@SuppressWarnings("serial")
public class NoSuchCustomerException extends RuntimeException{
	public NoSuchCustomerException(String msg) {
		super(msg);
	}
}
