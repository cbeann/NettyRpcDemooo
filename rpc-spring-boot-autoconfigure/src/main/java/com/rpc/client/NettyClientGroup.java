package com.rpc.client;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chaird
 * @create 2021-02-07 9:47
 */
@Data
public class NettyClientGroup {

  /** 下一个下标 */
  private AtomicInteger index = new AtomicInteger(0);

  /** 服务提供者名称 */
  private String providerName;

  /** 服务器 */
  List<NettyClient> providerList = new ArrayList<>();

  Map<String, NettyClient> providerMap = new HashMap<>();

  public NettyClientGroup(String providerName, List<String> providerStringList) {
    this.providerName = providerName;

    for (String s : providerStringList) {
      System.out.println(s);
      String[] split = s.split(":");
      String ip = split[0];
      Integer port = Integer.parseInt(split[1]);

      NettyClient nettyClient = new NettyClient(ip, port);
      nettyClient.start();

      providerMap.put(s, nettyClient);
      providerList.add(nettyClient);
    }
  }

  /**
   * 获取下一个客户端，轮询操作
   *
   * @return
   */
  public NettyClient next() {
    if (providerList.size() == 0) {
      return null;
    }
    return providerList.get(index.getAndIncrement() % providerList.size());
  }
}
