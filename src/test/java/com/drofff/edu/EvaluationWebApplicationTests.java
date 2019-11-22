package com.drofff.edu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluationWebApplicationTests {

	private static final String PROFILE_TABLE_NAME = "profile";
	private static final String USER_TABLE_NAME = "user_info";
	private static final String USER_ROLE_TABLE_NAME = "user_roles";
	private static final String GROUP_TABLE_NAME = "group_info";

	@Value("${spring.datasource.url}")
	private String connectionUrl;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;

	private Connection connection;

	@Before
	public void initConnection() throws Exception {
		connection = DriverManager.getConnection(connectionUrl, username, password);
	}

	@After
	public void closeConnection() throws Exception {
		connection.close();
	}

	@Test
	public void initAdmin() throws SQLException {
		if(isUserTableEmpty()) {
			System.out.println("Initializing admin user data");
			createAdmin();
		} else {
			System.out.println("There are users in database");
		}
	}

	private boolean isUserTableEmpty() throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement("select count(*) as user_count from " + USER_TABLE_NAME)) {
			try(ResultSet resultSet = preparedStatement.executeQuery()) {
				if(resultSet.next()) {
					return resultSet.getInt("user_count") == 0;
				}
				return true;
			}
		}
	}

	private void createAdmin() throws SQLException {
		Long id = (long) 0;
		Long groupId = (long) 1;
		String username = "admin";
		String password = encryptPassword("123456");
		createUser(id, username, password);
		createUserRole(id, "ADMIN");
		createGroup(groupId, "ADMINS");
		createUserProfile(id, groupId);
	}

	private String encryptPassword(String password) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(password);
	}

	private void createUser(Long id, String username, String password) throws SQLException {
		String query = "insert into " + USER_TABLE_NAME + " (id, username, password) values (?, ?, ?)";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, id);
			preparedStatement.setString(2, username);
			preparedStatement.setString(3, password);
			preparedStatement.executeUpdate();
		}
	}

	private void createUserRole(Long id, String role) throws SQLException {
		String query = "insert into " + USER_ROLE_TABLE_NAME + " (user_id, roles) values (?, ?)";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, id);
			preparedStatement.setString(2, role);
			preparedStatement.executeUpdate();
		}
	}

	private void createGroup(Long id, String name) throws SQLException {
		String query = "insert into " + GROUP_TABLE_NAME + " (id, name) values (?, ?)";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, id);
			preparedStatement.setString(2, name);
			preparedStatement.executeUpdate();
		}
	}

	private void createUserProfile(Long userId, Long groupId) throws SQLException {
		String query = "insert into " + PROFILE_TABLE_NAME + " (id, user_id, group_id, position) values (?, ?, ?, ?)";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, userId);
			preparedStatement.setLong(2, userId);
			preparedStatement.setLong(3, groupId);
			preparedStatement.setString(4, "Administrator");
			preparedStatement.executeUpdate();
		}
	}

}
