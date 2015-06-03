package com.github.culmat.easyjdbc.beans;


public class TrimStringsPostProcessor implements com.github.culmat.easyjdbc.beans.XBeanProcessor.PostProcessor<String>{

  @Override
  public String postProcess(String value) {
    return value == null ? "" : value.trim();
  }

  @Override
  public Class<String> registerTo() {
    return String.class;
  }

}
