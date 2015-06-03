package com.github.culmat.easyjdbc.test;

import static org.junit.Assert.assertEquals;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.culmat.easyjdbc.dao.SampleDao;

public class JEETest {

	private EJBContainer ec;
	private Context ctx;

	@Before
	public void before() throws Exception {
		ec = EJBContainer.createEJBContainer();
		ctx = ec.getContext();
	}

	@Test
	public void foo() throws Exception {
		SampleDao instance = (SampleDao) ctx.lookup("java:global/easyJDBC/SampleEJB");
		String result = instance.greet("World");
		assertEquals("Hello World", result);
	}

	@After
	public void tearDown() {
		if (ec != null) {
			ec.close();
		}
	}

}
