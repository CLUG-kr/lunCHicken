package com.clug.lunchicken.login.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

	private String ip, port, id, pw;
	public DataBase(String ip, String port, String id, String pw) throws SQLException {
		
	}
	
	public Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s", ip, port), id, pw);
		return conn;
	}
	
	public void closeConnection(ResultSet set, PreparedStatement pstmt, Connection  conn) throws SQLException {
		if (set != null)   set.close();
		if (pstmt != null) pstmt.close();
		if (conn != null)  conn.close();
	}
	
	public void closeConnection(PreparedStatement pstmt, Connection  conn) throws SQLException {
		if (pstmt != null) pstmt.close();
		if (conn != null)  conn.close();
	}
	
}
