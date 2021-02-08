package com.rpc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 2YSP
 * @date 2020/7/26 15:13
 */
@ConfigurationProperties(prefix = "rpc.server")
@Data
public class RpcServerProperties {

  /** 服务提供者名称 */
  private String providerName = "";

  /** 服务提供者名称 */
  private Integer providerPort = 9527;

  /** 权重，默认为1 */
  private Integer weight = 1;
}
