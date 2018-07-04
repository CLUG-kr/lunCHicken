package com.clug.lunchicken.login.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

	private String ip, port, id, pw;
	private AccountDAO account;
	public DataBase(String ip, String port, String id, String pw) throws SQLException {
		this.ip = ip;
		this.port = port;
		this.id = id;
		this.pw = pw;
		this.account = new AccountDAO(this);
	}
	
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Connection conn = null;
		String url = "jdbc:mysql://" + ip + ":"+ port + "/lunchicken?autoReconnect=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(url, id, pw);
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

	public AccountDAO getAccount() {
		return account;
	}
}
