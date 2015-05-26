package com.github.culmat.easyjdbc.dao;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.sql.DataSource;

@Stateless
@Local
public class SampleEJB implements SampleDao{
  private SampleDao sampleDao;

  @Resource(name = "jdbc/DataSource", mappedName = "TargetPersistenzDataSource", type = DataSource.class)
  private void setDatasource(DataSource datasource) {
    sampleDao = new SampleDaoImpl(datasource);
  }

}
