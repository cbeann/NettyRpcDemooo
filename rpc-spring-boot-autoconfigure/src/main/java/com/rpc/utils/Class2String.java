package com.rpc.utils;

/**
 * @author chaird
 * @create 2021-02-07 18:43
 */
public class Class2String {

  public static String[] class2String(Class<?>[] classes) {
    String[] parameterTypeString = new String[classes.length];
    for (int i = 0; i < classes.length; i++) {
      parameterTypeString[i] = classes[i].getName();
    }
    return parameterTypeString;
  }
}
