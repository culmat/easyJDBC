package com.github.culmat.easyjdbc.test;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.culmat.easyjdbc.PersistenceXmlConnectionHandler;

public class PersistenceXMLTest {

  protected DataSource datasource;

  @Before
  public void before() throws Exception {
 
    datasource = new PersistenceXmlConnectionHandler()
      .withSchemaName("differentScheme")
      .getDataSource("SamplePU");
  }

  @Test
  public void foo() {
    Assert.fail();
  }
}
