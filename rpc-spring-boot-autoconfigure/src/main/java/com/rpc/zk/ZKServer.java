package com.rpc.zk;

import cn.hutool.json.JSONUtil;
import com.rpc.model.ProviderBean;
import com.rpc.properties.RpcProperties;
import lombok.Data;
import org.apache.zookeeper.*;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author chaird
 * @create 2021-02-06 23:47
 */
@Data
public class ZKServer{


  private String ip;
  private Integer port;
  private String path;
  private String providerPath;
  private String consumerPath;

  ZooKeeper zk;

  /** 创建完改对象后创建目录 */
  @PostConstruct
  public void postConstruct() {
    // 在zk上创建目录
    createRpcPath();

    // 注册监听器
    //doRegisterWatcher();
  }

  /** 注册监听器 */
  @Deprecated
  private void doRegisterWatcher() {

    try {
      String listenProviderPath = path + providerPath;
      // 添加自定义监听器
      zk.register(null);
      zk.getChildren(listenProviderPath, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 在zk上创建目录 */
  private void createRpcPath() {
    try {
      // 创建rpc跟目录
      createPathPermanent(path, "");

      // 创建provider目录
      createPathPermanent(this.path + providerPath, "");

      // 创建consumer目录
      createPathPermanent(this.path + consumerPath, "");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ZKServer(RpcProperties rpcProperties) {

    this.ip = rpcProperties.getRegisterAddress();
    this.port = rpcProperties.getServerPort();
    this.path = rpcProperties.getPath();
    this.providerPath = rpcProperties.getProviderPath();
    this.consumerPath = rpcProperties.getConsumerPath();

    String url = ip + ":" + port;

    try {
      zk =
          new ZooKeeper(
              url,
              5000,
              new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                  // 获取事件的状态
                  Event.KeeperState keeperState = event.getState();
                  Event.EventType eventType = event.getType();
                  // 如果是建立连接
                  if (Event.KeeperState.SyncConnected == keeperState) {
                    if (Event.EventType.None == eventType) {
                      // 如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                      System.out.println("zk 建立连接");
                    }
                  }
                }
              });

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<String> getChild(String path) throws Exception {
    List<String> children = zk.getChildren(path, true);
    return children;
  }

  /**
   * 向zk注册服务提供者
   *
   * @param providerBean
   */
  public void sendProviderBeanMsg(ProviderBean providerBean) {

    String s = JSONUtil.toJsonStr(providerBean);
  }

  public String createPathPermanent(String path, String data) throws Exception {
    if (zk.exists(path, true) == null) {
      String mkPath =
          zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
      System.out.println("Success create path: " + mkPath);
      return mkPath;
    } else {
      return null;
    }
  }

  public String createPathTemp(String path, String data) throws Exception {
    if (zk.exists(path, true) == null) {
      String mkPath =
          zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
      System.out.println("Success create path: " + mkPath);
      return mkPath;
    } else {
      return null;
    }
  }


}
