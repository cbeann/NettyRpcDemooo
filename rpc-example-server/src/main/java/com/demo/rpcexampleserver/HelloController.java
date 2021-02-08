package com.demo.rpcexampleserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author chaird
 * @create 2021-02-07 0:33
 */
@RestController
public class HelloController {


  @GetMapping("/index")
  public Object hello() {
    return LocalDateTime.now().toString();
  }
}
