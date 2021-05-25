package com.example.rpcexampleserverclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rpc.annotation.RpcService;
import com.service.ServiceClientService;
import com.service.StudentService;

/**
 * Created on 2021-05-25
 */
@RestController
public class HelloController {


    @RpcService("provider02") // 自定义注解，其中value为服务提供者名称，类似OpenFeign的使用
    private ServiceClientService serviceClientService;


    @GetMapping("/index/{id}")
    public Object hello(@PathVariable String id) {
        String res = serviceClientService.getServerClient(id);
        return res;
    }
}
