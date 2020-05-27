package com.olenachernyak.couponsystem;

import model.Company;
import model.Customer;

public class Test {

	public static void main(String[] args) {
		AdminService as = new AdminService();
		Customer c = new Customer();
		c.setName("Nikita");
		c.setPassword("some1234");
		
		Company company = new Company();
		company.setName("Dandas");
		company.setPassword("54637");
		company.setEmail("4563@gdtr");

		try {
			as.insertNewCustomer(c, "admin", "1234");
			System.out.println(as.getAllCustomers("admin", "1234"));
			as.createCompany(company, "admin", "1234");
			System.out.println(as.getAllCompanies("admin", "1234"));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
