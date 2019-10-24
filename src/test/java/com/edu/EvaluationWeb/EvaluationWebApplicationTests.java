package com.edu.EvaluationWeb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

	@Test
	public void initAdmin() throws SQLException {
		if(isUserTableEmpty()) {
			System.out.println("Initializing admin user data");
			createAdmin();
		}
	}

	private boolean isUserTableEmpty() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("select count(*) as user_count from user");
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			return resultSet.getInt("user_count") == 0;
		}
		return true;
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
		String query = "insert into user (id, username, password) values (?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setLong(1, id);
		preparedStatement.setString(2, username);
		preparedStatement.setString(3, password);
		preparedStatement.executeUpdate();
	}

	private void createUserRole(Long id, String role) throws SQLException {
		String query = "insert into user_roles (user_id, roles) values (?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setLong(1, id);
		preparedStatement.setString(2, role);
		preparedStatement.executeUpdate();
	}

	private void createGroup(Long id, String name) throws SQLException {
		String query = "insert into group_info (id, name) values (?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setLong(1, id);
		preparedStatement.setString(2, name);
		preparedStatement.executeUpdate();
	}

	private void createUserProfile(Long userId, Long groupId) throws SQLException {
		String query = "insert into profile (id, user_id, group_id, position) values (?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setLong(1, userId);
		preparedStatement.setLong(2, userId);
		preparedStatement.setLong(3, groupId);
		preparedStatement.setString(4, "Administrator");
		preparedStatement.executeUpdate();
	}

}
