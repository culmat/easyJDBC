package com.github.culmat.easyjdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;



public abstract class AbstractConnectionHandler {
	
  public AbstractConnectionHandler() {
	  Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		@Override
		public void run() {
			close();			
		}
	}));
	  
  }

  protected void close() {
	for(Entry<String, PoolingDataSource> e : pools.entrySet()){
		  log.info("Shutting down "+e.getKey());
		  try {
			e.getValue().close();
		} catch (Exception e1) {
			log.fine(e1.getMessage());
		}
	  }
}

final Logger log = Logger.getLogger(getClass().getName());
  
  Map<String, PoolingDataSource> pools = new HashMap<>();
  String schemaName = null;
  
  public Connection getConnection(String name) throws SQLException {
    return getDataSource(name).getConnection();
  }

  public DataSource getDataSource(String name) throws SQLException {
    DataSource ds = pools.get(name);
    if(ds == null) {
      register(name);
      ds = pools.get(name);
    }
    return ds;
  }
  
  protected abstract void register(String name) throws SQLException;

  public void register(String name, Properties properties) throws SQLException {
    properties.put("user", properties.get("javax.persistence.jdbc.user"));
    properties.put("password", properties.get("javax.persistence.jdbc.password"));
    register(name, (String)properties.get("javax.persistence.jdbc.driver"), (String)properties.get("javax.persistence.jdbc.url"), properties);
  }

  public void register(String name, String driverClass, String jdbcUrl, String user, String password) throws SQLException {
    Properties properties = new Properties();
    properties.put("user", user);
    properties.put("password", password);
    register(name, driverClass, jdbcUrl, properties);
  }

  public void register(String name, String driverClass, String jdbcUrl) throws SQLException {
    register(name, driverClass, jdbcUrl, null);
  }
  public void register(String name, String driverClass, String jdbcUrl, Properties properties) throws SQLException {
    try {
      Class.forName(driverClass);
    }
    catch (ClassNotFoundException e) {
      throw new SQLException(e);
    }   
    
    ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbcUrl, properties);

    PoolableConnectionFactory poolableConnectionFactory =
        new PoolableConnectionFactory(connectionFactory, null);

   
    ObjectPool<PoolableConnection> connectionPool =
            new GenericObjectPool<>(poolableConnectionFactory);
    
    poolableConnectionFactory.setPool(connectionPool);

    PoolingDataSource<PoolableConnection> dataSource =
        new PoolingDataSource<PoolableConnection>(connectionPool) {
          @Override
          public Connection getConnection() throws SQLException {
        	  try { 
        		  return tryGetConnection();
        	  } catch (SQLException e) {
        		  log.fine(e.getMessage());
        		  log.fine("retrying");
        		  return tryGetConnection();
        	  }
          }

		private Connection tryGetConnection() throws SQLException {
			Connection ret = super.getConnection();
              if (schemaName != null) {
                ret.prepareCall("set schema " + schemaName).execute();
              }
              return ret;
		}
    };
    pools.put(name, dataSource);
  }

  public String getSchemaName() {
    return schemaName;
  }

  public <T extends AbstractConnectionHandler> T withSchemaName(String schemaName) {
    this.schemaName = schemaName;
    return (T)this;
  }

}
