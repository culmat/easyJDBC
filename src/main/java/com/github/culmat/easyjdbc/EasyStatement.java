package com.github.culmat.easyjdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

public abstract class EasyStatement<T> {

  private final transient Logger log = Logger.getLogger(getClass().getName());

  public static boolean closeConnection = true;
  public final String sql;

  public EasyStatement(String sql) {
    this.sql = sql;
  }

  public EasyStatement() {
    this.sql = readSQLString();
  }

  /**
   * Die Verarbeitung besteht typischer Weise aus drei Schritten
   * <ol>
   * <li>Parameter auf dem statement setzen, z.B. <code>stmt.setString(1, "Mustermann");</code></li>
   * <li>Statement ausführen, .z.B <code>stmt.executeQuery();</code><br/>
   * (Es sind explizit auch executeUpdate() u.ä. verwendbar)</li>
   * <li>ResultSet / Rückgabewerte verarbeiten</li>
   * </ol>
   * 
   * @param stmt
   * @return
   * @throws SQLException
   */
  protected abstract T execute(PreparedStatement stmt) throws SQLException;

  /**
   * @param statement
   * @return
   * @throws SQLException
   */
  public T execute(Connection con) throws SQLException {
    PreparedStatement stmt = null;
    try {
      stmt = con.prepareStatement(sql);
      return execute(stmt);
    }
    finally {
      close(stmt);
      if (closeConnection) close(con);
    }
  }

  protected void close(AutoCloseable closable) {
    try {
      if (closable != null) {
        closable.close();
      }
    }
    catch (Exception ex) {
      log.warning(ex.getLocalizedMessage());
    }
  }

  /**
   * @return den Inhalt der Resource getClass().getSimpleName() + ".sql" aus dem Klassenpfad.
   */
  protected String readSQLString() {
    String name = getClass().getSimpleName() + ".sql";
	InputStream is = getClass().getResourceAsStream(name);
    if(is == null) log.warning("Could not read resource " +name);
    @SuppressWarnings("resource")
    Scanner s = new Scanner(is).useDelimiter("\\A");
    try {
      return s.next();
    }
    finally {
      s.close();
    }
  }

}
