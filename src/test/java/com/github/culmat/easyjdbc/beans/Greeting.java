package com.github.culmat.easyjdbc.beans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.culmat.easyjdbc.EasyStatement;
import com.github.culmat.easyjdbc.beans.AnnotationXBeanProcessor;
import com.github.culmat.easyjdbc.beans.GreetingBean;

public class Greeting extends EasyStatement<GreetingBean> {

	AnnotationXBeanProcessor<GreetingBean> proc = new AnnotationXBeanProcessor<>(GreetingBean.class);
	private final String someone;

	public Greeting(String someone) {
		this.someone = someone;
	}

	@Override
	protected GreetingBean execute(PreparedStatement stmt) throws SQLException {
		stmt.setString(1, someone);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		return proc.toBean(rs);
	}

}
