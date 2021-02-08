package com.rpc.config;

import com.rpc.beanPostProcessor.RcpServiceInjectBeanPostProcessor;
import com.rpc.client.NettyClientBootStarp;
import com.rpc.properties.RpcClientProperties;
import com.rpc.properties.RpcProperties;
import com.rpc.properties.RpcServerProperties;
import com.rpc.server.NettyServer;
import com.rpc.zk.ZKServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chaird
 * @create 2021-02-07 0:24
 */
@Configuration
@EnableConfigurationProperties({
  RpcProperties.class,
  RpcServerProperties.class,
  RpcClientProperties.class
})
@ConditionalOnProperty(
    prefix = "rpc",
    name = "register-address",
    matchIfMissing =
        false) // 如果application.yml或者properties中没有myredis.host属性，则此类MyRedisAutoConfigulation不注入IOC容器
public class RpcAutoConfiguration {
  /**
   * 创建连接zk的客户端
   *
   * @param rpcProperties
   * @return
   */
  @Bean
  public ZKServer ZKServer(@Autowired RpcProperties rpcProperties) {
    ZKServer server = new ZKServer(rpcProperties);
    return server;
  }



  @Bean
  @ConditionalOnProperty(prefix = "rpc.server", name = "provider-name", matchIfMissing = false)
  public NettyServer nettyServer(@Autowired RpcServerProperties rpcServerProperties) {
    return new NettyServer(rpcServerProperties.getProviderPort());
  }

  @Bean
  @ConditionalOnProperty(prefix = "rpc.client", name = "consumer-name", matchIfMissing = false)
  public NettyClientBootStarp nettyClientBootStarp() {
    return new NettyClientBootStarp();
  }

  @Bean
  @ConditionalOnProperty(prefix = "rpc.client", name = "consumer-name", matchIfMissing = false)
  public RcpServiceInjectBeanPostProcessor rcpServiceInjectBeanPostProcessor() {
    return new RcpServiceInjectBeanPostProcessor();
  }
}
