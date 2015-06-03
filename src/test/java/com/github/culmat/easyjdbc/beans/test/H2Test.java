package com.github.culmat.easyjdbc.beans.test;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.culmat.easyjdbc.beans.Greeting;
import com.github.culmat.easyjdbc.beans.GreetingBean;

public class H2Test {


	private JdbcDataSource datasource;

	@Before
	public void before() throws Exception {
		 datasource = new JdbcDataSource();
		 datasource.setURL("jdbc:h2:mem:test");
	}

	@Test
	public void foo() throws Exception {
		String someone = "World";
		GreetingBean actual = new Greeting(someone).execute(datasource.getConnection());
		Assert.assertEquals("Hello World", actual.getGreeting());
	}
}
