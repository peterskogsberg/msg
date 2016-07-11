package com.skogsberg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

	/*
	 * Intentionally no public constructor; using Singleton pattern with
	 * accessor method due to SQLite concurrency limitations
	 */

	private static Connection connection = null;

	/*
	 * Lazy initialization
	 */
	public static Connection getInstance() {
		if (connection == null) {
			connection = setupConnection();
		}
		return connection;
	}

	private static Connection setupConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return connection;
	}

	private static Statement getStatement() throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		return statement;
	}

	public static ResultSet rawQuery(String query) throws SQLException {
		Statement statement = getStatement();
		ResultSet rs = statement.executeQuery(query);
		return rs;
	}
	
	public static void rawQueryWithoutResponse(String query) throws SQLException {
		Statement statement = getStatement();
		statement.executeUpdate(query);
	}
	
	/*
	 * Setup and dummy data generation below
	 */

	public static void setupTables() throws SQLException {
		Statement statement = getStatement();

		// A table storing the last timestamp a user retrieved messages
		statement.executeUpdate("drop table if exists lastsync");
		statement.executeUpdate("create table lastsync (name string primary key, timestamp integer)");

		// Table storing messages
		statement.executeUpdate("drop table if exists messages");
		statement.executeUpdate(
				"create table messages (id integer primary key, message string, recipient string, timestamp integer)");
	}

	public static void createDummyData() throws SQLException {
		Statement statement = getStatement();

		// Common placeholder names...
		// https://en.wikipedia.org/wiki/Alice_and_Bob

		// Some messages
		statement.executeUpdate(
				String.format("insert into messages values(null, 'How are you doing, Alice?', 'Alice', %s)",
						System.currentTimeMillis()));		
		statement.executeUpdate(
				String.format("insert into messages values(null, 'Bob, do you want to go for lunch?', 'Bob', %s)",
						System.currentTimeMillis()));
		
		// No sync history at first
		statement.executeUpdate("insert into lastsync values('Alice', 0)");
		statement.executeUpdate("insert into lastsync values('Bob', 0)");
	}

}
