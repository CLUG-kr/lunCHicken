package com.clug.lunchicken.login.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

	public static final int 
	ERR_UNKNOW = 1,
	
	SUCCESS_DUPLICATION_CHECK = 100,
	ERR_DUPLICATED_ID = 101,
	ERR_DUPLICATED_EMAIl = 102,
	
	SUCCESS_REGISTER = 200,
	
	SUCCESS_LOGIN = 300,
	ERR_WRONG_INFORMATION = 301
	;
	
	public DataBase db;
	public AccountDAO(DataBase db) {
		this.db = db;
	}
	
	public int login(AccountBean account) {
		return doLogin(account);
	}
	
	public int register(AccountBean account) {
		int ret = checkDuplication(account);
		if (ret == SUCCESS_DUPLICATION_CHECK) { // 성공했다면
			ret = doRegister(account);
		}
		return ret;
	}
	
	private int checkDuplication(AccountBean account) {
		Connection         conn  = null;
		PreparedStatement  pstmt = null;
		ResultSet          set   = null;
		String             query = "select * from account where account_id = ? OR email = ?";
		int 	           ret   = SUCCESS_DUPLICATION_CHECK;
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, account.getId());
			pstmt.setString(2, account.getEmail());
			set = pstmt.executeQuery();
			if (set.next()) {
				
				String id = set.getString("account_id");
				String email = set.getString("account_email");
				if (id.equals(account.getId())) {
					ret = ERR_DUPLICATED_ID;
				}
				else if (email.equals(account.getEmail())) {
					ret = ERR_DUPLICATED_EMAIl;
				}
				
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			ret = ERR_UNKNOW;
		} 
		try {
			db.closeConnection(set, pstmt, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private int doRegister(AccountBean account) {
		Connection         conn  = null;
		PreparedStatement  pstmt = null;
		String             query = "insert into `account` (`account_id`, `account_pw`, `account_email`) values(?, ?, ?)";
		int 	           ret   = SUCCESS_DUPLICATION_CHECK;
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, account.getId());
			pstmt.setString(2, account.getPw());
			pstmt.setString(3, account.getEmail());
			ret = pstmt.executeUpdate();
			if (ret != 0) {
				ret = SUCCESS_REGISTER;								
			}
			else {
				ret = ERR_UNKNOW;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			ret = ERR_UNKNOW;
		} 
		try {
			db.closeConnection(pstmt, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;

	}
	
	private int doLogin(AccountBean account) {
		Connection         conn  = null;
		PreparedStatement  pstmt = null;
		ResultSet          set   = null;
		String             query = "select * from account where account_id = ? AND account_pw = ?";
		int 	           ret   = SUCCESS_LOGIN;
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, account.getId());
			pstmt.setString(2, account.getEmail());
			set = pstmt.executeQuery();
			if (!set.next()) {
				ret = ERR_WRONG_INFORMATION;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			ret = ERR_UNKNOW;
		} 
		try {
			db.closeConnection(set, pstmt, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
