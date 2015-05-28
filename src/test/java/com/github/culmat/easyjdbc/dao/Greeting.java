package com.github.culmat.easyjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.culmat.easyjdbc.EasyStatement;

public class Greeting extends EasyStatement<String> {

	private final String someone;

	public Greeting(String someone) {
		this.someone = someone;
	}

	@Override
	protected String execute(PreparedStatement stmt) throws SQLException {
		stmt.setString(1, someone);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		return rs.getString(1);
	}

}
