package com.github.culmat.easyjdbc.dao.test;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.culmat.easyjdbc.dao.SampleDao;
import com.github.culmat.easyjdbc.dao.SampleDaoImpl;

public class H2Test {

	private SampleDao dao;

	@Before
	public void before() throws Exception {
		 JdbcDataSource datasource = new JdbcDataSource();
		 datasource.setURL("jdbc:h2:mem:test");
		 dao = new SampleDaoImpl(datasource);
	}

	@Test
	public void foo() throws Exception {
		String someone = "World";
		String actual = dao.greet(someone);
		Assert.assertEquals("Hello World", actual);
	}
}
