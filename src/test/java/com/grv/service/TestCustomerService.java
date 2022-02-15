package com.grv.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.grv.modal.Customer;

@SpringBootTest
public class TestCustomerService {
	@Autowired
	private CustomerService service;
	
	@BeforeEach
	public void before() {
		service.deleteAllCustomers();
	}
	
	@Test
	public void testSaveCustomer() {
		Customer customer = new Customer(1L, "Gaurav", "Singh");
		Customer saveCustomer = service.saveCustomer(customer);
		System.out.println(saveCustomer.getId());
	}
	
	@Test
	public void testSaveAllCustomers() {
		List<Customer> customers = new ArrayList<>();
		Customer customer1 = new Customer(2L, "Susant", "Singh");
		Customer customer2 = new Customer(3L, "Devender", "Singh");
		customers.addAll(Arrays.asList(customer1, customer2));
		
		Iterable<Customer> allCustomers = service.saveAllCustomers(customers);
		allCustomers.forEach(customer->{
			System.out.println(customer);
		});
		
		int insertSize = ((Collection<Customer>)allCustomers).size();
		boolean res = insertSize>0;
		assertEquals(true, res);
	}
}
