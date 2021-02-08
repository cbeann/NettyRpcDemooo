package com.rpc.utils;

/**
 * @author chaird
 * @create 2021-02-07 18:42
 */
public class String2Class {

  public static Class<?>[] string2Class(String[] parameterTypeStrings) throws Exception {
    Class<?>[] parameterTypes = new Class<?>[parameterTypeStrings.length];
    for (int i = 0; i < parameterTypeStrings.length; i++) {
      parameterTypes[i] = Class.forName(parameterTypeStrings[i]);
    }
    return parameterTypes;
  }
}
