package com.demo.rpcexampleserver.service.impl;

import com.service.StudentService;
import org.springframework.stereotype.Component;

/**
 * @author chaird
 * @create 2021-02-07 16:11
 */
@Component
public class StudentServiceImpl implements StudentService {

  @Override
  public String getId(Integer id) {
    return "StudentServiceImpl---get:" + id;
  }
}
