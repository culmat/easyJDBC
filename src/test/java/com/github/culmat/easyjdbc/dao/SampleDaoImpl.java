package com.github.culmat.easyjdbc.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

public class SampleDaoImpl implements SampleDao {

	private DataSource datasource;

	public SampleDaoImpl(DataSource datasource) {
		this.datasource = datasource;
	}

	@Override
	public String greet(String someone) throws SQLException {
		return new Greeting(someone).execute(datasource.getConnection());
	}

}
