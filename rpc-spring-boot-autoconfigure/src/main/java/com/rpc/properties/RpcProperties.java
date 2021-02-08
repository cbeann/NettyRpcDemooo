package com.rpc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chaird
 * @create 2021-02-07 2:36
 */
@ConfigurationProperties(prefix = "rpc")
@Data
public class RpcProperties {

  /** 服务注册中心地址 */
  private String registerAddress = "127.0.0.1";

  /** 服务暴露端口 */
  private Integer serverPort = 2181;

  /** 服务提供者保存地址 */
  private String path = "/rpc";

  /** 服务提供者保存地址 */
  private String consumerPath = "/consumer";

  /** 服务提供者保存地址 */
  private String providerPath = "/provider";


}
