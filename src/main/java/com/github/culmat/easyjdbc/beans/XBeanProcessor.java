package com.github.culmat.easyjdbc.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.BeanProcessor;

public class XBeanProcessor extends BeanProcessor {

  public static interface PostProcessor<T> {
    public T postProcess(T value);
    public Class<T> registerTo();
  }

  Map<Class<?>, PostProcessor<?>> postProcessors = new HashMap<Class<?>, XBeanProcessor.PostProcessor<?>>();

  public XBeanProcessor() {
    super();
  }

  public XBeanProcessor(Map<String, String> columnToPropertyOverrides) {
    super(columnToPropertyOverrides);
  }

  public XBeanProcessor addPostProcessor(PostProcessor<?> processor) {
    postProcessors.put(processor.registerTo(), processor);
    return this;
  }

  @SuppressWarnings("unchecked")
  protected Object processColumn(ResultSet rs, int index, Class<?> propType) throws SQLException {
    final Object ret = super.processColumn(rs, index, propType);
    @SuppressWarnings("rawtypes")
    PostProcessor postProc = postProcessors.get(propType);
    return postProc == null ? ret : postProc.postProcess(ret);
  }

}
