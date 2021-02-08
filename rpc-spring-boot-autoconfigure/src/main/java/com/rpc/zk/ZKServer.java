package com.rpc.zk;

import cn.hutool.json.JSONUtil;
import com.rpc.model.ProviderBean;
import com.rpc.properties.RpcProperties;
import lombok.Data;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

/**
 * @author chaird
 * @create 2021-02-06 23:47
 */
@Data
public class ZKServer implements ApplicationContextAware {

  private RpcProperties rpcProperties;
  private Watcher watcher;

  private String ip;
  private Integer port;
  private String path;
  private String providerPath;
  private String consumerPath;

  ZooKeeper zk;

  /** 创建完改对象后创建目录 */
  @PostConstruct
  public void postConstruct() {
    // 初始化
    init();
    // 在zk上创建目录
    createRpcPath();

    // 给某个目录添加监听器
    addWatcher();
  }

  /** 初始化 */
  private void init() {
    this.ip = rpcProperties.getRegisterAddress();
    this.port = rpcProperties.getServerPort();
    this.path = rpcProperties.getPath();
    this.providerPath = rpcProperties.getProviderPath();
    this.consumerPath = rpcProperties.getConsumerPath();

    String url = ip + ":" + port;

    try {
      zk = new ZooKeeper(url, 5000, watcher);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 给某个目录添加监听器 */
  private void addWatcher() {

    try {
      String listenProviderPath = path + providerPath;
      // 添加自定义监听器
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

  public ZKServer() {}

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

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.rpcProperties = applicationContext.getBean(RpcProperties.class);
    this.watcher = applicationContext.getBean(Watcher.class);
  }


  @Override
  public String toString() {
    return "ZKServer{" +
            "rpcProperties=" + rpcProperties +
            ", ip='" + ip + '\'' +
            ", port=" + port +
            ", path='" + path + '\'' +
            ", providerPath='" + providerPath + '\'' +
            ", consumerPath='" + consumerPath + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ZKServer zkServer = (ZKServer) o;
    return Objects.equals(rpcProperties, zkServer.rpcProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rpcProperties);
  }
}
