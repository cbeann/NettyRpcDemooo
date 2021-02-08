package com.rpc.annotation;

import java.lang.annotation.*;

/**
 * @author CBeann
 * @create 2020-03-09 22:47
 */
@Target(ElementType.FIELD) // 方法注解
@Retention(RetentionPolicy.RUNTIME) // 运行时注解
public @interface RpcService {

  String value();
}
