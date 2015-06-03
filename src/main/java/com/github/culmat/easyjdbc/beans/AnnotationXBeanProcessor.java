package com.github.culmat.easyjdbc.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.github.culmat.easyjdbc.beans.XBeanProcessor.PostProcessor;

public class AnnotationXBeanProcessor<T> {

  final static Logger log = Logger.getLogger(AnnotationXBeanProcessor.class.getName());
	  
  private XBeanProcessor proc;
  private Class<T> domainClass;

  public AnnotationXBeanProcessor(Class<T> domainClass) {
    this.domainClass = domainClass;
    proc = new XBeanProcessor(BeanInspector.inspect(domainClass));
  }

  public List<T> toBeanList(ResultSet rs) throws SQLException {
    return proc.toBeanList(rs, domainClass);
  }

  public T toBean(ResultSet rs) throws SQLException {
    return proc.toBean(rs, domainClass);
  }

  public AnnotationXBeanProcessor<T> addPostProcessor(PostProcessor<?> processor) {
    proc.addPostProcessor(processor);
    return this;
  }

  @SuppressWarnings("unchecked")
  public static class BeanInspector {

    @SuppressWarnings("rawtypes")
    static Class columnClass;
    static Method columnClassName;

    public static Map<String, String> inspect(Class<?> clazz) {
      if (columnClass == null) loadColumClass();
      Map<String, String> ret = new HashMap<String, String>();
      for (PropertyDescriptor ps : getPropertyDescriptors(clazz)) {
        Field field = getField(ps);
        if (field != null && field.isAnnotationPresent(columnClass)) {
          Annotation column = field.getAnnotation(columnClass);
          try {
            ret.put(columnClassName.invoke(column).toString(), field.getName());
          }
          catch (IllegalAccessException | InvocationTargetException e) {
            log.severe(e.getMessage());
          }
        }
      }
      return ret;
    }

    private static void loadColumClass() {
      try {
        columnClass = Class.forName("javax.persistence.Column");
        columnClassName = columnClass.getMethod("name");
      }
      catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
        throw new IllegalStateException("javax.persistence.Column must be on the classpath to use AnnotationXBeanProcessor", e);
      }
    }

    private static Field getField(PropertyDescriptor ps) {
      try {
        return ps.getWriteMethod().getDeclaringClass().getDeclaredField(ps.getName());
      }
      catch (Exception e) {
        return null;
      }

    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> c) {
      // Introspector caches BeanInfo classes for better performance
      BeanInfo beanInfo = null;
      try {
        beanInfo = Introspector.getBeanInfo(c);

      }
      catch (IntrospectionException e) {
        throw new IllegalArgumentException("Bean introspection failed: " + e.getMessage());
      }
      return beanInfo.getPropertyDescriptors();
    }
  }
}
