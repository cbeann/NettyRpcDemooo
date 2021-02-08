package com.rpc.client;

import com.rpc.properties.RpcClientProperties;
import com.rpc.properties.RpcProperties;
import com.rpc.zk.ZKServer;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chaird
 * @create 2021-02-07 10:06
 */
@Data
public class NettyClientBootStarp implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  private ZKServer zkServer;
  /** client属性 */
  private RpcClientProperties rpcClientProperties;
  /** 全局属性 */
  private RpcProperties rpcProperties;

  /** 服务消费者名称 */
  private String consumerName;
  /** 和NettyServer建立连接的NettyClient */
  Map<String, NettyClientGroup> providers = new HashMap<>();

  public NettyClientBootStarp() {}

  @PostConstruct
  public void postConstruct() throws Exception {
    //连接NettyServer
    connectProviders();
  }



  /**
   * 连接NettyServer
   * @throws Exception
   */
  private void connectProviders() throws Exception {

    String providerPath = rpcProperties.getPath() + rpcProperties.getProviderPath();
    List<String> provders = zkServer.getChild(providerPath);

    for (String provderName : provders) {

      List<String> providerInstance = zkServer.getChild(providerPath + "/" + provderName);

      if (CollectionUtils.isEmpty(providerInstance)) {
        continue;
      }

      NettyClientGroup nettyClientGroup = new NettyClientGroup(provderName, providerInstance);

      providers.put(provderName, nettyClientGroup);
    }
  }



  /**
   * 给容器填充属性
   *
   * @param applicationContext
   * @throws BeansException
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.zkServer = applicationContext.getBean(ZKServer.class);
    this.rpcClientProperties = applicationContext.getBean(RpcClientProperties.class);
    this.rpcProperties = applicationContext.getBean(RpcProperties.class);

    this.consumerName = rpcClientProperties.getConsumerName();
  }
}
