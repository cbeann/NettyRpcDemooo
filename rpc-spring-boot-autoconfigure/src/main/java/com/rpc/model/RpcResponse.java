package com.rpc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chaird
 * @create 2021-02-06 23:47
 */
@Data
public class RpcResponse implements Serializable {

  private String requestId;

  private Object returnValue;

  /**
   * 没有服务提供者
   *
   * @return
   */
  public static RpcResponse NO_SERVICE() {
    RpcResponse rpcResponse = new RpcResponse();
    rpcResponse.setRequestId("404Service");
    rpcResponse.setReturnValue("没有服务提供者");
    return rpcResponse;
  }
  /**
   * 没有服务提供者
   *
   * @return
   */
  public static RpcResponse TIME_OUT(String requestId) {
    RpcResponse rpcResponse = new RpcResponse();
    rpcResponse.setRequestId(requestId);
    rpcResponse.setReturnValue("超时TimeOut");
    return rpcResponse;
  }
}
