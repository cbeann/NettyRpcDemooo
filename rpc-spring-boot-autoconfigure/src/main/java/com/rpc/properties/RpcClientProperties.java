package com.rpc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 2YSP
 * @date 2020/7/26 15:13
 */
@ConfigurationProperties(prefix = "rpc.client")
@Data
public class RpcClientProperties {

  /** 服务提供者名称 */
  private String consumerName = "";



}
