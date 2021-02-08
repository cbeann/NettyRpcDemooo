package com.rpc.constant;

import com.rpc.client.RpcFuture;
import com.rpc.model.RpcResponse;

import java.util.HashMap;

/**
 * @author chaird
 * @create 2021-02-08 0:09
 */
public class FuturePool {

  public static HashMap<String, RpcFuture<RpcResponse>> pool = new HashMap();

  public static void put(String key, RpcFuture future) {
    pool.put(key, future);
  }

  public static RpcFuture<RpcResponse> get(String key) {
    return pool.get(key);
  }

  public static void remove(String key) {
    pool.remove(key);
  }
}
