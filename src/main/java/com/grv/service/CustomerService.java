package com.grv.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.grv.modal.Customer;

@Service
public class CustomerService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Customer saveCustomer(Customer customer) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement("INSERT INTO CUSTOMER(FIRST_NAME, LAST_NAME) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, customer.getFirstName());
				statement.setString(2, customer.getLastName());
				return statement;
			}
		}, holder);
		customer.setId(holder.getKey().longValue());
		return customer;
	
	}
	
	public Iterable<Customer> saveAllCustomers(List<Customer> customers) {
		jdbcTemplate.batchUpdate("INSERT INTO CUSTOMER(FIRST_NAME, LAST_NAME) VALUES(?, ?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Customer customer = customers.get(i);
				ps.setString(1, customer.getFirstName());
				ps.setString(2, customer.getLastName());
			}
			
			@Override
			public int getBatchSize() {
				return customers.size();
			}
		});
		
		Iterable<Customer> customerId = getCustomerId(customers);
		
		return customerId;
	}
	
	public Iterable<Customer> getCustomerId(List<Customer> customers){
		List<Customer> getCustomersId = new ArrayList<Customer>();
		for(Customer customer: customers) {
			String sql = "SELECT * FROM CUSTOMER WHERE first_name='"+customer.getFirstName()+"' and last_name='"+customer.getLastName()+"'";
			Customer customer1 = jdbcTemplate.queryForObject(sql, new CustomerMapper());
			getCustomersId.add(customer1);
		}
		return getCustomersId;
	}
	
	public int deleteAllCustomers() {
		int deletedRows = jdbcTemplate.update("DELETE FROM CUSTOMER");
		return deletedRows;
	}
	
	public static final class CustomerMapper implements RowMapper<Customer>{

		@Override
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer customer = new Customer();
			customer.setId(rs.getLong(1));
			customer.setFirstName(rs.getString(2));
			customer.setLastName(rs.getString(3));
			return customer;
		}
	}
}
