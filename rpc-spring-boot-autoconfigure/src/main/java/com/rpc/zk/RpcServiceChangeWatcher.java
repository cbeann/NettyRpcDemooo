package com.rpc.zk;

import com.rpc.client.NettyClientBootStarp;
import com.rpc.properties.RpcProperties;
import lombok.Data;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author chaird
 * @create 2021-02-09 0:17
 */
@Data
public class RpcServiceChangeWatcher implements Watcher, ApplicationContextAware {

  // IOC容器
  private ApplicationContext applicationContext;

  private RpcProperties rpcProperties;
  private NettyClientBootStarp nettyClientBootStarp;
  private ZKServer zkServer;

  private String listenProviderPath;

  @Override
  public void process(WatchedEvent event) {
    System.out.println(event);

    // 实际业务

    // 重新监听
    String providersPath = rpcProperties.getPath() + rpcProperties.getProviderPath();
    try {
      zkServer.getZk().getChildren(providersPath, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.rpcProperties = applicationContext.getBean(RpcProperties.class);
    this.nettyClientBootStarp = applicationContext.getBean(NettyClientBootStarp.class);
    this.zkServer = applicationContext.getBean(ZKServer.class);
  }
}
