package com.demo.rpcexampleclient.controller;

import com.rpc.annotation.RpcService;
import com.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaird
 * @create 2021-02-07 10:23
 */
@RestController
public class HelloController {

  @RpcService("provider01") // 自定义注解，其中value为服务提供者名称，类似OpenFeign的使用
  private StudentService studentService;


  @GetMapping("/index/{id}")
  public Object hello(@PathVariable Integer id) {
    String res = studentService.getId(id);

    return res;
  }
}
