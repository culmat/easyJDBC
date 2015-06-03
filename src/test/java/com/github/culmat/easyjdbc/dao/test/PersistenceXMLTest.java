package com.github.culmat.easyjdbc.dao.test;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.culmat.easyjdbc.PersistenceXmlConnectionHandler;
import com.github.culmat.easyjdbc.dao.SampleDao;
import com.github.culmat.easyjdbc.dao.SampleDaoImpl;

public class PersistenceXMLTest {

  private SampleDao dao;

	@Before
	public void before() throws Exception {
		DataSource datasource = new PersistenceXmlConnectionHandler().getDataSource("SamplePU");
		dao = new SampleDaoImpl(datasource);
	}

	@Test
	public void foo() throws Exception {
		String someone = "World";
		String actual = dao.greet(someone );
		Assert.assertEquals("Hello World", actual);
	}
}
