package com.demo.rpcexampleclient.controller;

import com.rpc.annotation.RpcService;
import com.rpc.client.NettyClientBootStarp;
import com.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaird
 * @create 2021-02-07 10:23
 */
@RestController
public class HelloController {

  @RpcService("provider01")
  private StudentService studentService;

  @Autowired private NettyClientBootStarp nettyClientBootStarp;

  @GetMapping("/index/{id}")
  public Object hello(@PathVariable Integer id) {
    String res = studentService.getId(id);

    return res;
  }
}
