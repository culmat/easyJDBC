package com.github.culmat.easyjdbc.dao;

import java.sql.SQLException;

public interface SampleDao {
	String greet(String someone) throws SQLException;
}
