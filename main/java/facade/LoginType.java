package facade;

public enum LoginType {
ADMIN(1), COMPANY(2), CUSTOMER(3);
	
	

	private int value;
	
	private LoginType(int value) {

		this.value = value;
	}
	
	public int getValue() {

		return value;
	}
}
