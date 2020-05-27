package com.olenachernyak.couponsystem;

import java.util.Collection;
import java.util.Collections;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import common.SystemMalfunctionException;
import dao.NoSuchCompanyException;
import dao.NoSuchCustomerException;
import facade.AbsFacade;
import facade.AdminFacade;
import facade.CompanyAlreadyExistsException;
import facade.CustomerAlreadyExistsException;
import facade.InvalidLoginException;
import facade.LoginType;
import model.Company;
import model.Customer;

@Path("admin")
public class AdminService {

	@POST
	@Path("insertNewCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertNewCustomer(Customer customer, @QueryParam("user") String name,
			@QueryParam("password") String password) {
		try {
			AdminFacade facade = (AdminFacade) AbsFacade.login(name, password, LoginType.ADMIN);
					
			facade.createCustomer(customer);

			return "Customer created!";
		} catch (InvalidLoginException | NoSuchCompanyException | dao.InvalidLoginException | SystemMalfunctionException | CustomerAlreadyExistsException | NoSuchCustomerException e) {
			return "Failed create customer!"+e.getMessage();
		}
	}

	@GET
	@Path("getAllCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Customer> getAllCustomers(@QueryParam("user") String name,
			@QueryParam("password") String password) {

		try {
			AdminFacade facade = (AdminFacade) AbsFacade.login(name, password, LoginType.ADMIN);
			return facade.getAllCustomers();
		} catch (InvalidLoginException | SystemMalfunctionException | NoSuchCompanyException | dao.InvalidLoginException
				| NoSuchCustomerException e) {
			return Collections.emptyList();
		}

	}

	@GET
	@Path("getAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Company> getAllCompanies(@QueryParam("user") String name,
			@QueryParam("password") String password) {
		try {
			AdminFacade facade = (AdminFacade) AbsFacade.login(name, password, LoginType.ADMIN);
			return facade.getAllCompanies();
		} catch (InvalidLoginException | SystemMalfunctionException | NoSuchCompanyException
				| dao.InvalidLoginException e) {
			return Collections.emptyList();
		}
	}

	@POST
	@Path("createCompany")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String createCompany(Company company, @QueryParam("user") String name,
			@QueryParam("password") String password) {
		try {
			AdminFacade facade = (AdminFacade) AbsFacade.login(name, password, LoginType.ADMIN);
			facade.createCompany(company);
			return "A company inserted";
		} catch (InvalidLoginException | SystemMalfunctionException | NoSuchCompanyException | dao.InvalidLoginException
				| CompanyAlreadyExistsException e) {
			return "Failed create company";
		}

	}
}