package com.github.culmat.easyjdbc;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;

import javax.persistence.spi.PersistenceUnitInfo;

import com.orientechnologies.orient.object.jpa.parsing.PersistenceXmlUtil;

public class PersistenceXmlConnectionHandler extends AbstractConnectionHandler {

  @Override
  protected void register(String name) throws SQLException {
    URL url = PersistenceXmlConnectionHandler.class.getResource("/META-INF/persistence.xml");
    Collection<? extends PersistenceUnitInfo> parse = PersistenceXmlUtil.parse(url);
    for (PersistenceUnitInfo info : parse) {
      if(name.equals(info.getPersistenceUnitName())){
        register(name, info.getProperties());
        return;
      }
    }
    System.err.println("Persistence Unit not found: " + name);
  }

}
