package facade;
@SuppressWarnings("serial")
public class CompanyAlreadyExistsException extends Exception {
public CompanyAlreadyExistsException(String msg) {
	super(msg);
}
}
