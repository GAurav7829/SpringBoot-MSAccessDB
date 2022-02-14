package com.grv.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.grv.modal.Customer;

@Service
public class CustomerService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public int saveCustomer(Customer customer) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		int id = jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement("INSERT INTO CUSTOMER(FIRST_NAME, LAST_NAME) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, "Gaurav");
				statement.setString(2, "Singh");
				return statement;
			}
		}, holder);
		
		int primaryKey = holder.getKey().intValue();
		return primaryKey;
	
	}
	
	public int[] saveAllCustomers(List<Customer> customers) {
		int[] batchUpdate = jdbcTemplate.batchUpdate("INSERT INTO CUSTOMER(FIRST_NAME, LAST_NAME) VALUES(?, ?)", new BatchPreparedStatementSetter() {
			
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
		return batchUpdate;
	}
	
	public int deleteAllCustomers() {
		int deletedRows = jdbcTemplate.update("DELETE FROM CUSTOMER");
		return deletedRows;
	}
}
