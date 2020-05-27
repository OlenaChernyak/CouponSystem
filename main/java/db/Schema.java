package db;

public class Schema {
	/* Table names. */
	private static final String TABLE_NAME_COUPON = "Coupon";
	private static final String TABLE_NAME_CUSTOMER = "Customer";
	private static final String TABLE_NAME_COMPANY = "Company";
	private static final String TABLE_NAME_CUSTOMER_COUPON = "Customer_Coupon";
	private static final String TABLE_NAME_COMPANY_COUPON = "Company_Coupon";

	/* Common column names. */
	private static final String COL_ID = "id";
	private static final String COL_NAME = "name";
	private static final String COL_PASSWORD = "password";
	/* Coupon columns. */
	private static final String COL_TITLE = "title";
	private static final String COL_START_DATE = "startDate";
	private static final String COL_END_DATE = "endDate";
	private static final String COL_AMOUNT = "amount";
	private static final String COL_CATEGORY = "category";
	private static final String COL_MESSAGE = "message";
	private static final String COL_PRICE = "price";
	private static final String COL_IMAGE_URL = "imageURL";
	private static final String COL_EMAIL = "email";

	private static final String COL_CUSTOMER_ID = "customer_id";
	private static final String COL_COUPON_ID = "coupon_id";
	private static final String COL_COMPANY_ID = "company_id";

	public static final String CREATE_TABLE_COUPON = "create table if not exists " + TABLE_NAME_COUPON + "(" + COL_ID
			+ " integer primary key auto_increment, " + COL_TITLE + " varchar(100), " + COL_START_DATE + " date, "
			+ COL_END_DATE + " date, " + COL_AMOUNT + " integer, " + COL_CATEGORY + " integer, " + COL_MESSAGE
			+ " varchar(255), " + COL_PRICE + " double, " + COL_IMAGE_URL + " varchar(100)" + ");";

	private static final String CREATE_TABLE_CUSTOMER = "create table if not exists " + TABLE_NAME_CUSTOMER + "("
			+ COL_ID + " integer primary key auto_increment," + COL_NAME + " varchar(50), " + COL_PASSWORD
			+ " varchar(100)) ";

	private static final String CREATE_TABLE_CUSTOMER_COUPON = "create table if not exists "
			+ TABLE_NAME_CUSTOMER_COUPON + "(" + COL_CUSTOMER_ID + " integer, " + COL_COUPON_ID
			+ " integer, foreign key (" + COL_CUSTOMER_ID + ") references " + TABLE_NAME_CUSTOMER + "(" + COL_ID
			+ ") on delete cascade, foreign key (" + COL_COUPON_ID + ") references " + TABLE_NAME_COUPON + "(" + COL_ID
			+ ") on delete cascade, primary key (" + COL_CUSTOMER_ID + ", " + COL_COUPON_ID + "));";

	private static final String CREATE_CUSTOMER = "insert into " + TABLE_NAME_CUSTOMER + " (" + COL_NAME + ", "
			+ COL_PASSWORD + ") values (?,?)";

	private static final String CREATE_COMPANY = "insert into " + TABLE_NAME_COMPANY + " (" + COL_NAME + ", "
			+ COL_PASSWORD + ", " + COL_EMAIL + ") values (?,?,?)";

	private static final String REMOVE_COMPANY = "delete from " + TABLE_NAME_COMPANY + " where " + COL_ID + " = ?";

	private static final String UPDATE_COMPANY = "update " + TABLE_NAME_COMPANY + " set " + COL_NAME + " = ?, "
			+ COL_PASSWORD + " = ?, " + COL_EMAIL + " = ? where " + COL_ID + " = ?";

	private static final String GET_COMPANY_COUPONS = "select * from " + TABLE_NAME_COUPON + " t1 inner join "
			+ TABLE_NAME_COMPANY_COUPON + " t2 on t1." + COL_ID + " = t2." + COL_COUPON_ID + " where t2."
			+ COL_COMPANY_ID + " = ?";

	private static final String GET_ALL_COMAPNIES = "select " + COL_ID + " from " + TABLE_NAME_COMPANY + "";

	private static final String GET_INSERT_COMPANY_COUPON = "insert into " + TABLE_NAME_COMPANY_COUPON + "("
			+ COL_COMPANY_ID + ", " + COL_COUPON_ID + ") values (?,?)";

	private static final String GET_SELECT_COMPANY = "select * from " + TABLE_NAME_COMPANY + " where " + COL_ID
			+ " = ?";

	private static final String GET_COMPANY_LOGIN = "select * from " + TABLE_NAME_COMPANY + " where " + COL_NAME
			+ " = ? and " + COL_PASSWORD + " = ?";

	private static final String REMOVE_CUSTOMER = "delete from " + TABLE_NAME_CUSTOMER + " where " + COL_ID + " = ?";

	private static final String UPDATE_CUSTOMER = "update " + TABLE_NAME_CUSTOMER + " set " + COL_NAME + " = ?, "
			+ COL_PASSWORD + " = ? where " + COL_ID + " = ?";

	public static final String UPDATE_COUPON = "update " + TABLE_NAME_COUPON + " set " + COL_TITLE + " = ?, "
			+ COL_START_DATE + " = ?, " + COL_END_DATE + " = ?, " + COL_AMOUNT + " = ?, " + COL_CATEGORY + " = ?, "
			+ COL_MESSAGE + " = ?, " + COL_PRICE + " = ?, " + COL_IMAGE_URL + " = ? where " + COL_ID + " = ?";

	private static final String GET_CUSTOMER_BY_ID = "select * from " + TABLE_NAME_CUSTOMER + " where " + COL_ID
			+ " = ?;";

	private static final String GET_CUSTOMER_BY_NAME_AND_PASSWORD = "select * from " + TABLE_NAME_CUSTOMER + " where "
			+ COL_NAME + " = ? and " + COL_PASSWORD + " = ?";

	private static final String GET_ALL_CUSTOMERS = "select * from " + TABLE_NAME_CUSTOMER;

	// private static final String GET_COUPONS_FROM_CUSTOMER = "select * from " +
	// TABLE_NAME_CUSTOMER_COUPON + "where " + COL_CUSTOMER_ID
	// + "= ?";
	private static final String GET_COUPONS_OF_CUSTOMER = "select * from " + TABLE_NAME_COUPON + " t1 inner join "
			+ TABLE_NAME_CUSTOMER_COUPON + " t2 on t1." + COL_ID + " = t2." + COL_COUPON_ID + " where t2."
			+ COL_CUSTOMER_ID + " = ?";

	private static final String GET_INSERT_CUSTOMER_COUPON = "insert into " + TABLE_NAME_CUSTOMER_COUPON + " ("
			+ COL_CUSTOMER_ID + ", " + COL_COUPON_ID + ") values (?,?)";

	private static final String CREATE_TABLE_COMPANY = "create table if not exists " + TABLE_NAME_COMPANY + "(" + COL_ID
			+ " integer primary key auto_increment, " + COL_NAME + " varchar(50), " + COL_PASSWORD + " varchar(50), "
			+ COL_EMAIL + " varchar (50))";

	private static String CREATE_TABLE_COMPANY_COUPON = "create table if not exists " + TABLE_NAME_COMPANY_COUPON + "( "
			+ COL_COMPANY_ID + " integer, " + COL_COUPON_ID + " integer, foreign key (" + COL_COMPANY_ID
			+ ") references " + TABLE_NAME_COMPANY + "(" + COL_ID + ") on delete cascade, foreign key (" + COL_COUPON_ID
			+ ") references " + TABLE_NAME_COUPON + "(" + COL_ID + ") on delete cascade, primary key(" + COL_COMPANY_ID
			+ ", " + COL_COUPON_ID + "));";

	private static final String CREATE_COUPON = "insert into " + TABLE_NAME_COUPON + "(" + COL_TITLE + ", "
			+ COL_START_DATE + ", " + COL_END_DATE + ", " + COL_AMOUNT + ", " + COL_CATEGORY + ", " + COL_MESSAGE + ", "
			+ COL_PRICE + ", " + COL_IMAGE_URL + ") values (?,?,?,?,?,?,?,?)";

	private static final String GET_REMOVE_COUPON = "delete from " + TABLE_NAME_COUPON + " where " + COL_ID + " = ?";

	private static final String DECREMENT_COUPON = "update " + TABLE_NAME_COUPON + " set " + COL_AMOUNT + " = "
			+ COL_AMOUNT + " - 1 where " + COL_ID + " = ? and " + COL_AMOUNT + " > 0";

	private static final String GET_COUPON = "select * from " + TABLE_NAME_COUPON + " where " + COL_ID + " = ?";

	private static final String GET_ALL_COUPONS = "select " + COL_ID + " from " + TABLE_NAME_COUPON + ";";

	private static final String GET_COUPONS_BY_CATEGORY = "select * from " + TABLE_NAME_COUPON + " where "
			+ COL_CATEGORY + " = ?";

	// Statement for CustomerDBDao
	public static String getCreateCustomerTable() {
		return CREATE_TABLE_CUSTOMER;
	}

	public static String getCreateCustomerCouponTable() {
		return CREATE_TABLE_CUSTOMER_COUPON;
	}

	public static String getCreateCustomer() {
		return CREATE_CUSTOMER;
	}

	public static String getRemoveCustomer() {
		return REMOVE_CUSTOMER;
	}

	public static String getUpdateCustomer() {
		return UPDATE_CUSTOMER;
	}

	public static String getCustomer() {
		return GET_CUSTOMER_BY_ID;
	}

	public static String getAllCustomers() {
		return GET_ALL_CUSTOMERS;
	}

	public static String getCouponsFromTableCustomer() {
		return GET_COUPONS_OF_CUSTOMER;
	}

	public static String getInsertCustomerCoupon() {
		return GET_INSERT_CUSTOMER_COUPON;
	}

	public static String getCustomerLogin() {
		return GET_CUSTOMER_BY_NAME_AND_PASSWORD;
	}

	// Statements for CompanyDBDao

	public static String getCreateTableCompany() {
		return CREATE_TABLE_COMPANY;
	}

	public static String getCreateTableCompanyCoupon() {
		return CREATE_TABLE_COMPANY_COUPON;
	}

	public static String getCreateCompany() {
		return CREATE_COMPANY;
	}

	public static String getRemoveCompany() {
		return REMOVE_COMPANY;
	}

	public static String getSelectCompany() {
		return GET_SELECT_COMPANY;
	}

	public static String getInsertIntoCompanyCoupon() {
		return GET_INSERT_COMPANY_COUPON;
	}

	public static String getUpdateCompanyById() {
		return UPDATE_COMPANY;
	}

	public static String getSelectCompanyCouponInnerJoinById() {
		return GET_COMPANY_COUPONS;
	}

	public static String getSelectIdAllCompanies() {
		return GET_ALL_COMAPNIES;
	}

	public static String getSelectCompanyByNamePassword() {
		return GET_COMPANY_LOGIN;
	}

	// Statements for CouponDBDao
	public static String getCreateTableCoupon() {
		return CREATE_TABLE_COUPON;
	}

	public static String getCreateCoupon() {
		return CREATE_COUPON;
	}

	public static String getRemoveCoupon() {
		return GET_REMOVE_COUPON;
	}

	public static String getUpdateCouponById() {
		return UPDATE_COUPON;
	}

	public static String getDecrementCouponAmountById() {
		return DECREMENT_COUPON;
	}

	public static String getCouponById() {
		return GET_COUPON;
	}

	public static String getAllCoupons() {
		return GET_ALL_COUPONS;
	}

	public static String getAllCouponsByCategory() {
		return GET_COUPONS_BY_CATEGORY;
	}
}
